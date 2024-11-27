package com.gmail.riteshbakare420.Service.Provider.System.service;

import com.gmail.riteshbakare420.Service.Provider.System.dto.*;
import com.gmail.riteshbakare420.Service.Provider.System.model.Booking;
import com.gmail.riteshbakare420.Service.Provider.System.model.ServiceProvider;
import com.gmail.riteshbakare420.Service.Provider.System.model.Slot;
import com.gmail.riteshbakare420.Service.Provider.System.repository.BookingRepository;
import com.gmail.riteshbakare420.Service.Provider.System.repository.ServiceProviderRepository;
import com.gmail.riteshbakare420.Service.Provider.System.repository.SlotRepository;
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
public class ServiceProviderService {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;
    @Autowired
    private SlotRepository slotRepository;

    //    public List<ServiceProvider> getAllServiceProviders() {
//        return serviceProviderRepository.findAll();
//    }
    public List<ServiceProviderResponseDTO> getAllServiceProviders() {
        List<ServiceProvider> providers = serviceProviderRepository.findAll();
        return providers.stream()
                .map(provider -> {
                    ServiceProviderResponseDTO dto = new ServiceProviderResponseDTO();
                    dto.setId(provider.getId());
                    dto.setName(provider.getName());
                    dto.setEmail(provider.getEmail());
                    dto.setPhone(provider.getPhone());
                    dto.setServiceType(provider.getServiceType());
                    dto.setSlots(provider.getSlots().stream()
                            .map(slot -> new SlotResponseDTO(
                                    slot.getId(),
                                    slot.getStartTime(),
                                    slot.getEndTime(),
                                    slot.isBooked()))
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ServiceProvider getServiceProviderById(Long id) {
        return serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service Provider not found"));
    }

    public void addServiceProvider(ServiceProviderDTO dto) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(dto.getName());
        serviceProvider.setEmail(dto.getEmail());
        serviceProvider.setPhone(dto.getPhone());
        serviceProvider.setServiceType(dto.getServiceType());
        serviceProviderRepository.save(serviceProvider);
    }

    public void addSlots(Long serviceProviderId, List<SlotDTO> slotDTOs) {
        ServiceProvider serviceProvider = getServiceProviderById(serviceProviderId);

        List<Slot> slots = slotDTOs.stream()
                .map(dto -> {
                    Slot slot = new Slot();
                    slot.setStartTime(dto.getStartTime());
                    slot.setEndTime(dto.getEndTime());
                    slot.setBooked(false);
                    slot.setServiceProvider(serviceProvider);
                    return slot;
                })
                .collect(Collectors.toList());

        slotRepository.saveAll(slots);
    }

    public List<SlotDTO> getAvailableSlots(Long providerId) {
        List<Slot> slots = slotRepository.findByServiceProviderIdAndIsBookedFalse(providerId);
        if (slots == null) {
            return new ArrayList<>();
        }
        return slots.stream()
                .map(slot -> new SlotDTO(slot.getStartTime(), slot.getEndTime(), slot.isBooked()))
                .collect(Collectors.toList());
    }

    public void deleteServiceProvider(Long serviceProviderId) {
        serviceProviderRepository.deleteById(serviceProviderId);
    }

    public List<BookingDetailsDTO> getServiceProviderBookings(Long serviceProviderId) {
        ServiceProvider serviceProvider = getServiceProviderById(serviceProviderId);

        return serviceProvider.getBookings().stream()
                .map(booking -> convertToBookingDetailsDTO(booking))
                .collect(Collectors.toList());
    }

    public List<BookingDetailsDTO> getServiceProviderBookingsByStatus(
            Long serviceProviderId,
            String status) {
        ServiceProvider serviceProvider = getServiceProviderById(serviceProviderId);

        return serviceProvider.getBookings().stream()
                .filter(booking -> booking.getStatus().equalsIgnoreCase(status))
                .map(booking -> convertToBookingDetailsDTO(booking))
                .collect(Collectors.toList());
    }

    public List<BookingDetailsDTO> getServiceProviderBookingsByDateRange(
            Long serviceProviderId,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        ServiceProvider serviceProvider = getServiceProviderById(serviceProviderId);

        return serviceProvider.getBookings().stream()
                .filter(booking ->
                        booking.getBookingTime().isAfter(startDate) &&
                                booking.getBookingTime().isBefore(endDate))
                .map(booking -> convertToBookingDetailsDTO(booking))
                .collect(Collectors.toList());
    }

    public BookingDetailsDTO updateBookingStatus(
            Long serviceProviderId,
            Long bookingId,
            String newStatus) {
        ServiceProvider serviceProvider = getServiceProviderById(serviceProviderId);

        Booking booking = serviceProvider.getBookings().stream()
                .filter(b -> b.getId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStatus(newStatus.toUpperCase());
        bookingRepository.save(booking);

        return convertToBookingDetailsDTO(booking);
    }

    private BookingDetailsDTO convertToBookingDetailsDTO(Booking booking) {
        CustomerDetailsDTO customerDTO = new CustomerDetailsDTO(
                booking.getCustomer().getId(),
                booking.getCustomer().getName(),
                booking.getCustomer().getEmail(),
                booking.getCustomer().getPhone()
        );

        SlotDetailsDTO slotDTO = new SlotDetailsDTO(
                booking.getSlot().getId(),
                booking.getSlot().getStartTime(),
                booking.getSlot().getEndTime(),
                booking.getSlot().isBooked()
        );

        return new BookingDetailsDTO(
                booking.getId(),
                customerDTO,
                slotDTO,
                booking.getBookingTime(),
                booking.getStatus()
        );
    }
}