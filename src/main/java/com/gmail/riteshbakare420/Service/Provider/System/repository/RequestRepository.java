package com.gmail.riteshbakare420.Service.Provider.System.repository;

import com.gmail.riteshbakare420.Service.Provider.System.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserIdAndIsRequestAcceptedFalse(Long userId);
}
