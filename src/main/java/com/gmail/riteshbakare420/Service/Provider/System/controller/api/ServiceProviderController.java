package com.gmail.riteshbakare420.Service.Provider.System.controller.api;

import com.gmail.riteshbakare420.Service.Provider.System.dto.BookingDetailsDTO;
import com.gmail.riteshbakare420.Service.Provider.System.dto.ServiceProviderDTO;
import com.gmail.riteshbakare420.Service.Provider.System.dto.ServiceProviderResponseDTO;
import com.gmail.riteshbakare420.Service.Provider.System.dto.SlotDTO;
import com.gmail.riteshbakare420.Service.Provider.System.model.ServiceProvider;
import com.gmail.riteshbakare420.Service.Provider.System.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/service-providers")
public class ServiceProviderController {
    @Autowired
    private ServiceProviderService serviceProviderService;

    @GetMapping("/all")
    public ResponseEntity<List<ServiceProviderResponseDTO>> getAllServiceProviders() {
        return ResponseEntity.ok(serviceProviderService.getAllServiceProviders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProvider> getDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceProviderService.getServiceProviderById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addServiceProvider(
            @RequestBody ServiceProviderDTO serviceProviderDTO) {
        serviceProviderService.addServiceProvider(serviceProviderDTO);
        return ResponseEntity.ok("Service Provider added successfully");
    }

    @PostMapping("/{serviceProviderId}/slots")
    public ResponseEntity<String> addSlots(
            @PathVariable Long serviceProviderId,
            @RequestBody List<SlotDTO> slots) {
        serviceProviderService.addSlots(serviceProviderId, slots);
        return ResponseEntity.ok("Slots added successfully");
    }

    @GetMapping("/{serviceProviderId}/available-slots")
    public ResponseEntity<List<SlotDTO>> getAvailableSlots(
            @PathVariable Long serviceProviderId) {
        return ResponseEntity.ok(serviceProviderService.getAvailableSlots(serviceProviderId));
    }

    @DeleteMapping("/{serviceProviderId}")
    public ResponseEntity<String> deleteServiceProvider(
            @PathVariable Long serviceProviderId) {
        serviceProviderService.deleteServiceProvider(serviceProviderId);
        return ResponseEntity.ok("Service Provider deleted successfully");
    }

    @GetMapping("/{serviceProviderId}/bookings")
    public ResponseEntity<List<BookingDetailsDTO>> getServiceProviderBookings(
            @PathVariable Long serviceProviderId) {
        List<BookingDetailsDTO> bookings = serviceProviderService.getServiceProviderBookings(serviceProviderId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{serviceProviderId}/bookings/status/{status}")
    public ResponseEntity<List<BookingDetailsDTO>> getBookingsByStatus(
            @PathVariable Long serviceProviderId,
            @PathVariable String status) {
        List<BookingDetailsDTO> bookings =
                serviceProviderService.getServiceProviderBookingsByStatus(serviceProviderId, status);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{serviceProviderId}/bookings/date-range")
    public ResponseEntity<List<BookingDetailsDTO>> getBookingsByDateRange(
            @PathVariable Long serviceProviderId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<BookingDetailsDTO> bookings =
                serviceProviderService.getServiceProviderBookingsByDateRange(
                        serviceProviderId, startDate, endDate);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{serviceProviderId}/bookings/{bookingId}/status")
    public ResponseEntity<BookingDetailsDTO> updateBookingStatus(
            @PathVariable Long serviceProviderId,
            @PathVariable Long bookingId,
            @RequestBody String newStatus) {
        BookingDetailsDTO updatedBooking =
                serviceProviderService.updateBookingStatus(serviceProviderId, bookingId, newStatus);
        return ResponseEntity.ok(updatedBooking);
    }

}
