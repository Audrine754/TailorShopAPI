package com.auca.tailorshop.repository;

import com.auca.tailorshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByCustomerId(Long customerId);
}