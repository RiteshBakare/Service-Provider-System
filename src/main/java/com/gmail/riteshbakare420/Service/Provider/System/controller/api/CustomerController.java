package com.gmail.riteshbakare420.Service.Provider.System.controller.api;

import com.gmail.riteshbakare420.Service.Provider.System.dto.CustomerDTO;
import com.gmail.riteshbakare420.Service.Provider.System.model.Customer;
import com.gmail.riteshbakare420.Service.Provider.System.service.CustomerService;
import com.gmail.riteshbakare420.Service.Provider.System.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.addCustomer(customerDTO);
        return ResponseEntity.ok("Customer added successfully");
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<String> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customerId, customerDTO);
        return ResponseEntity.ok("Customer updated successfully");
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok("Customer deleted successfully");
    }

    @PostMapping("/{customerId}/book/{serviceProviderId}")
    public ResponseEntity<String> bookService(
            @PathVariable Long customerId,
            @PathVariable Long serviceProviderId,
            @RequestParam Long slotId) {
        try {
            customerService.bookService(customerId, serviceProviderId, slotId);
            return ResponseEntity.ok("Service booked successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


}
