package com.gmail.riteshbakare420.Service.Provider.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String userType; // "CUSTOMER" or "SERVICE_PROVIDER"
    private String serviceType; // Only for service providers
}
