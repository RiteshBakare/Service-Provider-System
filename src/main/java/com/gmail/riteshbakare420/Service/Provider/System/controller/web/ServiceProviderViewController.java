package com.gmail.riteshbakare420.Service.Provider.System.controller.web;

import com.gmail.riteshbakare420.Service.Provider.System.dto.*;
import com.gmail.riteshbakare420.Service.Provider.System.model.*;
import com.gmail.riteshbakare420.Service.Provider.System.service.CustomerService;
import com.gmail.riteshbakare420.Service.Provider.System.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/service-provider-view")
public class ServiceProviderViewController {

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private CustomerService customerService;

    // to do --> service-provider-view
    @GetMapping("/{serviceProviderId}")
    public String getServiceProviderView(@PathVariable Long serviceProviderId, Model model) {
        ServiceProvider serviceProvider = serviceProviderService.getServiceProviderById(serviceProviderId);
        List<SlotDTO> availableSlots = serviceProviderService.getAvailableSlots(serviceProviderId);
        List<Booking> bookings = serviceProvider.getBookings();
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();
        List<RequestBooking> requestBookings = serviceProvider.getRequestBookings();


        model.addAttribute("serviceProvider", serviceProvider);
        model.addAttribute("availableSlots", availableSlots);
        model.addAttribute("bookings", bookings);
        model.addAttribute("customers", customers);
        model.addAttribute("requestBookings", requestBookings);
        return "service-provider-view";
    }

    @PostMapping("/{serviceProviderId}/slots")
    public String addSlots(@PathVariable Long serviceProviderId, @ModelAttribute SlotDTO slotDTO, Model model) {
        serviceProviderService.addSlots(serviceProviderId, List.of(slotDTO));
        return "redirect:/service-provider-view/{serviceProviderId}";
    }

    @PostMapping("/{serviceProviderId}/bookings/{bookingId}/status")
    public String updateBookingStatus(@PathVariable Long serviceProviderId,
                                      @PathVariable Long bookingId,
                                      @RequestParam String newStatus,
                                      Model model) {
        serviceProviderService.updateBookingStatus(serviceProviderId, bookingId, newStatus);
        return "redirect:/service-provider-view/{serviceProviderId}";
    }

    @GetMapping("/{providerId}/customer/{customerId}/slots")
    public String getProviderSlots(@PathVariable Long customerId,
                                   @PathVariable Long providerId,
                                   Model model) {
        try {
            Customer customer = customerService.getCustomerById(customerId);
            ServiceProvider provider = serviceProviderService.getServiceProviderById(providerId);
            List<RequestSlotResponseDTO> availableSlots = customer.getRequestSlots().stream()
                    .filter(slot -> !slot.isRequestAccepted())
                    .map(slot -> new RequestSlotResponseDTO(
                            slot.getId(),
                            slot.getStartTime(),
                            slot.getEndTime(),
                            slot.isRequestAccepted(),
                            slot.getRequestedService()
                    ))
                    .collect(Collectors.toList());

            model.addAttribute("customer", customer);
            model.addAttribute("provider", provider);
            model.addAttribute("availableSlots", availableSlots);

            return "customer-request-slots";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{providerId}/book")
    public String bookService(@PathVariable Long providerId,
                              @RequestParam Long customerId,
                              @RequestParam Long requestId,
                              RedirectAttributes redirectAttributes) {
        try {
            serviceProviderService.acceptRequestedService(customerId, providerId, requestId);
            redirectAttributes.addFlashAttribute("successMessage", "Request accepted successfully!");
            return "redirect:/service-provider-view/" + providerId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/service-provider-view/" + providerId;
        }
    }
}

//@RestController
//@RequestMapping("/service-provider-view")
//public class ServiceProviderViewController {
//    @Autowired
//    private ServiceProviderService serviceProviderService;
//
//    @GetMapping("/all")
//    public ResponseEntity<List<ServiceProviderResponseDTO>> getAllServiceProviders() {
//        return ResponseEntity.ok(serviceProviderService.getAllServiceProviders());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ServiceProvider> getDetailsById(@PathVariable Long id) {
//        return ResponseEntity.ok(serviceProviderService.getServiceProviderById(id));
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addServiceProvider(
//            @RequestBody ServiceProviderDTO serviceProviderDTO) {
//        serviceProviderService.addServiceProvider(serviceProviderDTO);
//        return ResponseEntity.ok("Service Provider added successfully");
//    }
//
//    @PostMapping("/{serviceProviderId}/slots")
//    public ResponseEntity<String> addSlots(
//            @PathVariable Long serviceProviderId,
//            @RequestBody List<SlotDTO> slots) {
//        serviceProviderService.addSlots(serviceProviderId, slots);
//        return ResponseEntity.ok("Slots added successfully");
//    }
//
//    @GetMapping("/{serviceProviderId}/available-slots")
//    public ResponseEntity<List<SlotDTO>> getAvailableSlots(
//            @PathVariable Long serviceProviderId) {
//        return ResponseEntity.ok(serviceProviderService.getAvailableSlots(serviceProviderId));
//    }
//
//    @DeleteMapping("/{serviceProviderId}")
//    public ResponseEntity<String> deleteServiceProvider(
//            @PathVariable Long serviceProviderId) {
//        serviceProviderService.deleteServiceProvider(serviceProviderId);
//        return ResponseEntity.ok("Service Provider deleted successfully");
//    }
//
//    @GetMapping("/{serviceProviderId}/bookings")
//    public ResponseEntity<List<BookingDetailsDTO>> getServiceProviderBookings(
//            @PathVariable Long serviceProviderId) {
//        List<BookingDetailsDTO> bookings = serviceProviderService.getServiceProviderBookings(serviceProviderId);
//        return ResponseEntity.ok(bookings);
//    }
//
//    @GetMapping("/{serviceProviderId}/bookings/status/{status}")
//    public ResponseEntity<List<BookingDetailsDTO>> getBookingsByStatus(
//            @PathVariable Long serviceProviderId,
//            @PathVariable String status) {
//        List<BookingDetailsDTO> bookings =
//                serviceProviderService.getServiceProviderBookingsByStatus(serviceProviderId, status);
//        return ResponseEntity.ok(bookings);
//    }
//
//    @GetMapping("/{serviceProviderId}/bookings/date-range")
//    public ResponseEntity<List<BookingDetailsDTO>> getBookingsByDateRange(
//            @PathVariable Long serviceProviderId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        List<BookingDetailsDTO> bookings =
//                serviceProviderService.getServiceProviderBookingsByDateRange(
//                        serviceProviderId, startDate, endDate);
//        return ResponseEntity.ok(bookings);
//    }
//
//    @PutMapping("/{serviceProviderId}/bookings/{bookingId}/status")
//    public ResponseEntity<BookingDetailsDTO> updateBookingStatus(
//            @PathVariable Long serviceProviderId,
//            @PathVariable Long bookingId,
//            @RequestBody String newStatus) {
//        BookingDetailsDTO updatedBooking =
//                serviceProviderService.updateBookingStatus(serviceProviderId, bookingId, newStatus);
//        return ResponseEntity.ok(updatedBooking);
//    }
//}
