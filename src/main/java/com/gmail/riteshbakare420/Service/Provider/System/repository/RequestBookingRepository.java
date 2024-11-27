package com.gmail.riteshbakare420.Service.Provider.System.repository;

import com.gmail.riteshbakare420.Service.Provider.System.model.RequestBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestBookingRepository extends JpaRepository<RequestBooking, Long> {
}
