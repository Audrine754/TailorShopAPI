package com.auca.tailorshop.repository;

import com.auca.tailorshop.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    boolean existsByCustomerId(Long customerId);
}