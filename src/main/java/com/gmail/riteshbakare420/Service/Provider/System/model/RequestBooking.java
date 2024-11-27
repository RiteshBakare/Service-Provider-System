package com.gmail.riteshbakare420.Service.Provider.System.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reuquestbookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference(value = "customer-request-bookings")
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonBackReference(value = "provider-request-bookings")
    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private ServiceProvider serviceProvider;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request requestSlot;

    private LocalDateTime requestBookingTime;
    private String status;
}
