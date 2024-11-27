package com.gmail.riteshbakare420.Service.Provider.System.repository;

import com.gmail.riteshbakare420.Service.Provider.System.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
