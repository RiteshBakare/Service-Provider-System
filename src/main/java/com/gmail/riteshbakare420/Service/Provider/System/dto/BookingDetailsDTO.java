package com.gmail.riteshbakare420.Service.Provider.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailsDTO {
    private Long bookingId;
    private CustomerDetailsDTO customerDetails;
    private SlotDetailsDTO slotDetails;
    private LocalDateTime bookingTime;
    private String status;
}
