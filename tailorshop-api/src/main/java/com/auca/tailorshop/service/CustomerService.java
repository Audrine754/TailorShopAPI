package com.auca.tailorshop.service;

import com.auca.tailorshop.entity.Customer;
import com.auca.tailorshop.exception.BusinessException;
import com.auca.tailorshop.exception.ResourceNotFoundException;
import com.auca.tailorshop.repository.CustomerRepository;
import com.auca.tailorshop.repository.MeasurementRepository;
import com.auca.tailorshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final MeasurementRepository measurementRepository;

    public CustomerService(CustomerRepository customerRepository,
                           OrderRepository orderRepository,
                           MeasurementRepository measurementRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.measurementRepository = measurementRepository;
    }

    public Customer create(Customer customer) {
        // Business rule: phone number must be unique
        if (customerRepository.existsByPhone(customer.getPhone())) {
            throw new BusinessException("A customer with phone " + customer.getPhone() + " already exists");
        }
        customer.setId(null);
        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
    }

    public Customer update(Long id, Customer updated) {
        Customer existing = findById(id);
        // Business rule: changing phone must not clash with another customer
        if (!existing.getPhone().equals(updated.getPhone())
                && customerRepository.existsByPhone(updated.getPhone())) {
            throw new BusinessException("Phone " + updated.getPhone() + " is already used by another customer");
        }
        existing.setFullName(updated.getFullName());
        existing.setPhone(updated.getPhone());
        existing.setEmail(updated.getEmail());
        existing.setAddress(updated.getAddress());
        return customerRepository.save(existing);
    }

    public void delete(Long id) {
        Customer existing = findById(id);
        // Business rule: cannot delete a customer who has orders or measurements
        if (orderRepository.existsByCustomerId(id)) {
            throw new BusinessException("Cannot delete customer with existing orders");
        }
        if (measurementRepository.existsByCustomerId(id)) {
            throw new BusinessException("Cannot delete customer with existing measurements");
        }
        customerRepository.delete(existing);
    }
}