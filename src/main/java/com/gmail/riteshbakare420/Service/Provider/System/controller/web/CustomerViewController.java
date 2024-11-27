package com.gmail.riteshbakare420.Service.Provider.System.controller.web;

import com.gmail.riteshbakare420.Service.Provider.System.dto.ServiceProviderResponseDTO;
import com.gmail.riteshbakare420.Service.Provider.System.dto.SlotDTO;
import com.gmail.riteshbakare420.Service.Provider.System.dto.SlotResponseDTO;
import com.gmail.riteshbakare420.Service.Provider.System.model.Booking;
import com.gmail.riteshbakare420.Service.Provider.System.model.Customer;
import com.gmail.riteshbakare420.Service.Provider.System.model.ServiceProvider;
import com.gmail.riteshbakare420.Service.Provider.System.service.CustomerService;
import com.gmail.riteshbakare420.Service.Provider.System.service.ServiceProviderService;
import com.gmail.riteshbakare420.Service.Provider.System.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/customer-view")
public class CustomerViewController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ServiceProviderService serviceProviderService;


    @GetMapping("/{customerId}")
    public String getCustomerView(@PathVariable Long customerId, Model model) {
        try {
            Customer customer = customerService.getCustomerById(customerId);
            List<ServiceProviderResponseDTO> serviceProviders = serviceProviderService.getAllServiceProviders();
            List<Booking> bookings = customer.getBookings();

            model.addAttribute("customer", customer);
            model.addAttribute("serviceProviders", serviceProviders);
            model.addAttribute("bookings", bookings);

            return "customer-dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }


    @GetMapping("/{customerId}/provider/{providerId}/slots")
    public String getProviderSlots(@PathVariable Long customerId,
                                   @PathVariable Long providerId,
                                   Model model) {
        try {
            Customer customer = customerService.getCustomerById(customerId);
            ServiceProvider provider = serviceProviderService.getServiceProviderById(providerId);
            List<SlotResponseDTO> availableSlots = provider.getSlots().stream()
                    .filter(slot -> !slot.isBooked())
                    .map(slot -> new SlotResponseDTO(
                            slot.getId(),
                            slot.getStartTime(),
                            slot.getEndTime(),
                            slot.isBooked()
                    ))
                    .collect(Collectors.toList());

            model.addAttribute("customer", customer);
            model.addAttribute("provider", provider);
            model.addAttribute("availableSlots", availableSlots);

            return "provider-slots";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }


    @PostMapping("/{customerId}/book")
    public String bookService(@PathVariable Long customerId,
                              @RequestParam Long serviceProviderId,
                              @RequestParam Long slotId,
                              RedirectAttributes redirectAttributes) {
        try {
            customerService.bookService(customerId, serviceProviderId, slotId);
            redirectAttributes.addFlashAttribute("successMessage", "Service booked successfully!");
            return "redirect:/customer-view/" + customerId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customer-view/" + customerId;
        }
    }
}

//@Controller
//@RequestMapping("/customer-view")
//public class CustomerViewController {
//
//    @Autowired
//    private CustomerService customerService;
//
//    @Autowired
//    private ServiceProviderService serviceProviderService;
//
//    @GetMapping("/{customerId}")
//    public String getCustomerView(@PathVariable Long customerId, Model model) {
//        try {
//            Customer customer = customerService.getCustomerById(customerId);
//            if (customer == null) {
//                throw new ResourceNotFoundException("Customer not found with ID: " + customerId);
//            }
//            List<Booking> bookings = customer.getBookings();
//            List<ServiceProviderResponseDTO> serviceProviders = serviceProviderService.getAllServiceProviders();
//
//            model.addAttribute("customer", customer);
//            model.addAttribute("bookings", bookings);
//            model.addAttribute("serviceProviders", serviceProviders);
//
//            return "customer-view";
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "error";
//        }
//    }
//
//    @GetMapping("/{customerId}/book/{serviceProviderId}")
//    public String bookService(@PathVariable Long customerId,
//                              @PathVariable Long serviceProviderId,
////                              @PathVariable Long slotId,
//                              Model model) {
//        try {
//            customerService.bookService(customerId, serviceProviderId, 2l);
//            return "redirect:/customer-view/{customerId}";
//        } catch (ResourceNotFoundException e) {
//            model.addAttribute("errorMessage", "Booking failed: " + e.getMessage());
//            return "error";
//        } catch (IllegalStateException e) {
//            model.addAttribute("errorMessage", "Invalid state: " + e.getMessage());
//            return "error";
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Unexpected error: " + e.getMessage());
//            return "error";
//        }
//    }
//}


//@RestController
//@RequestMapping("/customer-view")
//public class CustomerViewController {
//
//    @Autowired
//    CustomerService customerService;
//
//    @Autowired
//    ServiceProviderService serviceProviderService;
//
//    @GetMapping("id/{customerId}")
//    public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId) {
//        return ResponseEntity.ok(customerService.getCustomerById(customerId));
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customerDTO) {
//        customerService.addCustomer(customerDTO);
//        return ResponseEntity.ok("Customer added successfully");
//    }
//
//    @PutMapping("id/{customerId}")
//    public ResponseEntity<String> updateCustomer(
//            @PathVariable Long customerId,
//            @RequestBody CustomerDTO customerDTO) {
//        customerService.updateCustomer(customerId, customerDTO);
//        return ResponseEntity.ok("Customer updated successfully");
//    }
//
//    @PostMapping("/{customerId}/book/{serviceProviderId}")
//    public ResponseEntity<String> bookService(
//            @PathVariable Long customerId,
//            @PathVariable Long serviceProviderId,
//            @RequestParam Long slotId) {
//        try {
//            customerService.bookService(customerId, serviceProviderId, slotId);
//            return ResponseEntity.ok("Service booked successfully");
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<ServiceProviderResponseDTO>> getAllServiceProviders() {
//        return ResponseEntity.ok(serviceProviderService.getAllServiceProviders());
//    }
//
//    @GetMapping("details/{id}")
//    public ResponseEntity<ServiceProvider> getDetailsById(@PathVariable Long id) {
//        return ResponseEntity.ok(serviceProviderService.getServiceProviderById(id));
//    }
//
//}
