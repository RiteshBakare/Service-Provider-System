package com.gmail.riteshbakare420.Service.Provider.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBookingDetailsDTO {
    private Long requestBookingId;
    private CustomerDetailsDTO customerDetails;
    private RequestDetailsDTO requestDetails;
    private LocalDateTime bookingTime;
    private String status;
}
