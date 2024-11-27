package com.gmail.riteshbakare420.Service.Provider.System.controller.web;

import com.gmail.riteshbakare420.Service.Provider.System.dto.UserRegistrationDTO;
import com.gmail.riteshbakare420.Service.Provider.System.model.Customer;
import com.gmail.riteshbakare420.Service.Provider.System.model.ServiceProvider;
import com.gmail.riteshbakare420.Service.Provider.System.model.User;
import com.gmail.riteshbakare420.Service.Provider.System.repository.CustomerRepository;
import com.gmail.riteshbakare420.Service.Provider.System.repository.ServiceProviderRepository;
import com.gmail.riteshbakare420.Service.Provider.System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @GetMapping("/login")
    public String login() {
        return "login"; // Create this login.html template
    }

    @GetMapping("/login-success")
    public String loginSuccess(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_CUSTOMER")) {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
            return "redirect:/customer-view/" + user.getCustomer().getId();
        } else if (role.equals("ROLE_SERVICE_PROVIDER")) {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
            return "redirect:/service-provider-view/" + user.getServiceProvider().getId();
        } else if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin-view/home";
        }

        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userDTO", new UserRegistrationDTO());
        return "register"; // Create this register.html template
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegistrationDTO userDTO,
                               RedirectAttributes redirectAttributes) {
        // Implement user registration logic
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getUserType().equals("CUSTOMER")) {
            Customer customer = new Customer();
            customer.setName(userDTO.getName());
            customer.setEmail(userDTO.getEmail());
            customer.setPhone(userDTO.getPhone());
            customerRepository.save(customer);

            user.setCustomer(customer);
            user.setRole("CUSTOMER");
        } else if (userDTO.getUserType().equals("SERVICE_PROVIDER")) {
            ServiceProvider serviceProvider = new ServiceProvider();
            serviceProvider.setName(userDTO.getName());
            serviceProvider.setEmail(userDTO.getEmail());
            serviceProvider.setPhone(userDTO.getPhone());
            serviceProvider.setServiceType(userDTO.getServiceType());
            serviceProviderRepository.save(serviceProvider);

            user.setServiceProvider(serviceProvider);
            user.setRole("SERVICE_PROVIDER");
        }

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Registration Successful!");
        return "redirect:/login";
    }
}
