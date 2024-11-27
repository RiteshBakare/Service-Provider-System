package com.gmail.riteshbakare420.Service.Provider.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isRequestAccepted;
    private String requestedService;
}
