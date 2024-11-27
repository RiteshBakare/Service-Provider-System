package com.gmail.riteshbakare420.Service.Provider.System.service;

import com.gmail.riteshbakare420.Service.Provider.System.dto.CustomerDTO;
import com.gmail.riteshbakare420.Service.Provider.System.model.Booking;
import com.gmail.riteshbakare420.Service.Provider.System.model.Customer;
import com.gmail.riteshbakare420.Service.Provider.System.model.ServiceProvider;
import com.gmail.riteshbakare420.Service.Provider.System.model.Slot;
import com.gmail.riteshbakare420.Service.Provider.System.repository.BookingRepository;
import com.gmail.riteshbakare420.Service.Provider.System.repository.CustomerRepository;
import com.gmail.riteshbakare420.Service.Provider.System.repository.ServiceProviderRepository;
import com.gmail.riteshbakare420.Service.Provider.System.repository.SlotRepository;
import com.gmail.riteshbakare420.Service.Provider.System.utils.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
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
}
