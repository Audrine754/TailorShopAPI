package com.auca.tailorshop.service;

import com.auca.tailorshop.entity.Customer;
import com.auca.tailorshop.entity.Order;
import com.auca.tailorshop.exception.BusinessException;
import com.auca.tailorshop.exception.ResourceNotFoundException;
import com.auca.tailorshop.repository.CustomerRepository;
import com.auca.tailorshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    private static final List<String> VALID_STATUSES =
            Arrays.asList("PENDING", "IN_PROGRESS", "READY", "DELIVERED");

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    private Customer loadCustomer(Order order) {
        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            throw new BusinessException("Customer id is required, e.g. \"customer\": {\"id\": 1}");
        }
        Long customerId = order.getCustomer().getId();
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));
    }

    private String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "PENDING";
        }
        String s = status.trim().toUpperCase();
        if (!VALID_STATUSES.contains(s)) {
            throw new BusinessException("Status must be one of " + VALID_STATUSES);
        }
        return s;
    }

    public Order create(Order order) {
        Customer customer = loadCustomer(order);
        // Business rule: due date cannot be in the past
        if (order.getDueDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Due date cannot be in the past");
        }
        order.setId(null);
        order.setCustomer(customer);
        order.setStatus(normalizeStatus(order.getStatus()));
        return orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    public Order update(Long id, Order updated) {
        Order existing = findById(id);
        // Business rule: delivered orders are locked
        if ("DELIVERED".equals(existing.getStatus())) {
            throw new BusinessException("Delivered orders cannot be modified");
        }
        Customer customer = loadCustomer(updated);
        if (!updated.getDueDate().equals(existing.getDueDate())
                && updated.getDueDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Due date cannot be in the past");
        }
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setDueDate(updated.getDueDate());
        existing.setStatus(normalizeStatus(updated.getStatus()));
        existing.setCustomer(customer);
        return orderRepository.save(existing);
    }

    public void delete(Long id) {
        Order existing = findById(id);
        // Business rule: a delivered order is kept as a record
        if ("DELIVERED".equals(existing.getStatus())) {
            throw new BusinessException("Delivered orders cannot be deleted");
        }
        orderRepository.delete(existing);
    }
}