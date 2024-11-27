package com.gmail.riteshbakare420.Service.Provider.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String serviceType;
    private List<SlotResponseDTO> slots;
}
