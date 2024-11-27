package com.gmail.riteshbakare420.Service.Provider.System.repository;

import com.gmail.riteshbakare420.Service.Provider.System.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByServiceProviderIdAndIsBookedFalse(Long serviceProviderId);
}
