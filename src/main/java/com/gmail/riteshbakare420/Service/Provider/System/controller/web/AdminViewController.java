package com.gmail.riteshbakare420.Service.Provider.System.controller.web;

import com.gmail.riteshbakare420.Service.Provider.System.dto.*;
import com.gmail.riteshbakare420.Service.Provider.System.model.Customer;
import com.gmail.riteshbakare420.Service.Provider.System.model.ServiceProvider;
import com.gmail.riteshbakare420.Service.Provider.System.service.CustomerService;
import com.gmail.riteshbakare420.Service.Provider.System.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin-view")
public class AdminViewController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ServiceProviderService serviceProviderService;


    @GetMapping("/home")
    public String adminHome() {
        return "admin/home";
    }

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customers/list";
    }

    @GetMapping("/customers/add")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customerDTO", new CustomerDTO());
        return "admin/customers/add";
    }

    @PostMapping("/customers/add")
    public String addCustomer(@ModelAttribute CustomerDTO customerDTO, RedirectAttributes redirectAttributes) {
        try {
            customerService.addCustomer(customerDTO);
            redirectAttributes.addFlashAttribute("message", "Customer added successfully!");
            return "redirect:/admin-view/customers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding customer: " + e.getMessage());
            return "redirect:/admin-view/customers/add";
        }
    }

    @GetMapping("/customers/edit/{id}")
    public String showEditCustomerForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());

        model.addAttribute("customerId", id);
        model.addAttribute("customerDTO", customerDTO);
        return "admin/customers/edit";
    }

    @PostMapping("/customers/edit/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute CustomerDTO customerDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            customerService.updateCustomer(id, customerDTO);
            redirectAttributes.addFlashAttribute("message", "Customer updated successfully!");
            return "redirect:/admin-view/customers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating customer: " + e.getMessage());
            return "redirect:/admin-view/customers/edit/" + id;
        }
    }

    @PostMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("message", "Customer deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting customer: " + e.getMessage());
        }
        return "redirect:/admin-view/customers";
    }



    @GetMapping("/providers")
    public String listServiceProviders(Model model) {
        List<ServiceProviderResponseDTO> providers = serviceProviderService.getAllServiceProviders();
        model.addAttribute("providers", providers);
        return "admin/providers/list";
    }

    @GetMapping("/providers/add")
    public String showAddProviderForm(Model model) {
        model.addAttribute("providerDTO", new ServiceProviderDTO());
        return "admin/providers/add";
    }

    @PostMapping("/providers/add")
    public String addProvider(@ModelAttribute ServiceProviderDTO providerDTO,
                              RedirectAttributes redirectAttributes) {
        try {
            serviceProviderService.addServiceProvider(providerDTO);
            redirectAttributes.addFlashAttribute("message", "Service Provider added successfully!");
            return "redirect:/admin-view/providers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding service provider: " + e.getMessage());
            return "redirect:/admin-view/providers/add";
        }
    }

    @GetMapping("/providers/{id}/slots")
    public String showProviderSlots(@PathVariable Long id, Model model) {
        ServiceProvider provider = serviceProviderService.getServiceProviderById(id);
        List<SlotDTO> availableSlots = serviceProviderService.getAvailableSlots(id);

        model.addAttribute("provider", provider);
        model.addAttribute("slots", availableSlots);
        model.addAttribute("newSlot", new SlotDTO());
        return "admin/providers/slots";
    }

    @PostMapping("/providers/{id}/slots")
    public String addProviderSlots(@PathVariable Long id,
                                   @ModelAttribute SlotDTO slotDTO,
                                   RedirectAttributes redirectAttributes) {
        try {
            List<SlotDTO> slots = new ArrayList<>();
            slots.add(slotDTO);
            serviceProviderService.addSlots(id, slots);
            redirectAttributes.addFlashAttribute("message", "Slot added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding slot: " + e.getMessage());
        }
        return "redirect:/admin-view/providers/" + id + "/slots";
    }

    @GetMapping("/providers/{id}/bookings")
    public String showProviderBookings(@PathVariable Long id, Model model) {
        List<BookingDetailsDTO> bookings = serviceProviderService.getServiceProviderBookings(id);
        ServiceProvider provider = serviceProviderService.getServiceProviderById(id);

        model.addAttribute("provider", provider);
        model.addAttribute("bookings", bookings);
        return "admin/providers/bookings";
    }

    @PostMapping("/providers/delete/{id}")
    public String deleteProvider(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            serviceProviderService.deleteServiceProvider(id);
            redirectAttributes.addFlashAttribute("message", "Service Provider deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting service provider: " + e.getMessage());
        }
        return "redirect:/admin-view/providers";
    }
}

