package com.gmail.riteshbakare420.Service.Provider.System.repository;

import com.gmail.riteshbakare420.Service.Provider.System.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
}