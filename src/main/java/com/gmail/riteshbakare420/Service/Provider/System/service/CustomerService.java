package com.gmail.riteshbakare420.Service.Provider.System.service;

import com.gmail.riteshbakare420.Service.Provider.System.dto.*;
import com.gmail.riteshbakare420.Service.Provider.System.model.*;
import com.gmail.riteshbakare420.Service.Provider.System.repository.*;
import com.gmail.riteshbakare420.Service.Provider.System.utils.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private  RequestBookingRepository requestBookingRepository;

    public List<CustomerResponseDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customer -> {
            CustomerResponseDTO customerDTO = new CustomerResponseDTO();
            customerDTO.setId(customer.getId());
            customerDTO.setName(customer.getName());
            customerDTO.setEmail(customer.getEmail());
            return customerDTO;
        }).toList();
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public void addCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customerRepository.save(customer);
    }

    public void updateCustomer(Long customerId, CustomerDTO customerDTO) {
        Customer customer = getCustomerById(customerId);
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customerRepository.save(customer);
    }

    public void updateCustomerNew(CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer ID: " + customerDTO.getId()));
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    public void bookService(Long customerId, Long serviceProviderId, Long slotId) {
        Customer customer = getCustomerById(customerId);
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Service Provider not found"));
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (slot.isBooked()) {
            throw new IllegalStateException("Slot already booked");
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setServiceProvider(serviceProvider);
        booking.setSlot(slot);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("BOOKED");

        slot.setBooked(true);
        slotRepository.save(slot);
        bookingRepository.save(booking);
    }

    public ServiceProvider getServiceProviderById(Long id) {
        return serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service Provider not found"));
    }

    private RequestBookingDetailsDTO convertToRequestBookingDetailsDTO(RequestBooking booking) {
        CustomerDetailsDTO customerDTO = new CustomerDetailsDTO(
                booking.getCustomer().getId(),
                booking.getCustomer().getName(),
                booking.getCustomer().getEmail(),
                booking.getCustomer().getPhone()
        );

        RequestDetailsDTO slotDTO = new RequestDetailsDTO(
                booking.getRequestSlot().getId(),
                booking.getRequestSlot().getStartTime(),
                booking.getRequestSlot().getEndTime(),
                booking.getRequestSlot().isRequestAccepted()
        );

        return new RequestBookingDetailsDTO(
                booking.getId(),
                customerDTO,
                slotDTO,
                booking.getRequestBookingTime(),
                booking.getStatus()
        );
    }

    public RequestBookingDetailsDTO updateRequestBookingStatus(
            Long customerId,
            Long requestBookingId,
            String newStatus) {
        Customer customer = getCustomerById(customerId);

        RequestBooking requestBooking = customer.getRequestBookings().stream()
                .filter(b -> b.getId().equals(requestBookingId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        requestBooking.setStatus(newStatus.toUpperCase());
        requestBookingRepository.save(requestBooking);

        return convertToRequestBookingDetailsDTO(requestBooking);
    }

    public List<RequestBookingDetailsDTO> getCustomerRequestBookingsByDateRange(
            Long customerId,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        Customer customer = getCustomerById(customerId);

        return customer.getRequestBookings().stream()
                .filter(booking ->
                        booking.getRequestBookingTime().isAfter(startDate) &&
                                booking.getRequestBookingTime().isBefore(endDate))
                .map(this::convertToRequestBookingDetailsDTO)
                .collect(Collectors.toList());
    }

    public List<RequestBookingDetailsDTO> getCustomerRequestBookingsByStatus(
            Long customerId,
            String status) {
        Customer customer = getCustomerById(customerId);

        return customer.getRequestBookings().stream()
                .filter(booking -> booking.getStatus().equalsIgnoreCase(status))
                .map(this::convertToRequestBookingDetailsDTO)
                .collect(Collectors.toList());
    }

    public List<RequestBookingDetailsDTO> getCustomerRequestBookings(Long customerId) {
        Customer customer = getCustomerById(customerId);

        return customer.getRequestBookings().stream()
                .map(this::convertToRequestBookingDetailsDTO)
                .collect(Collectors.toList());
    }

    public List<RequestDTO> getAvailableRequestSlots(Long customerId) {
        List<Request> requestSlots = requestRepository.findByCustomerIdAndIsRequestAcceptedFalse(customerId);
        if (requestSlots == null) {
            return new ArrayList<>();
        }
        return requestSlots.stream()
                .map(slot -> new RequestDTO(slot.getStartTime(), slot.getEndTime(), slot.isRequestAccepted()))
                .collect(Collectors.toList());
    }

    public void addRequestSlots(Long customerId, List<RequestDTO> requestSlotDTOs) {
        Customer customer = getCustomerById(customerId);

        List<Request> requestSlots = requestSlotDTOs.stream()
                .map(dto -> {
                    Request slot = new Request();
                    slot.setStartTime(dto.getStartTime());
                    slot.setEndTime(dto.getEndTime());
                    slot.setRequestAccepted(false);
                    slot.setCustomer(customer);
                    return slot;
                })
                .toList();

        requestRepository.saveAll(requestSlots);
    }
}
