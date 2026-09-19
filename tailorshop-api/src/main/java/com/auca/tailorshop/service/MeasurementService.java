package com.auca.tailorshop.service;

import com.auca.tailorshop.entity.Customer;
import com.auca.tailorshop.entity.Measurement;
import com.auca.tailorshop.exception.BusinessException;
import com.auca.tailorshop.exception.ResourceNotFoundException;
import com.auca.tailorshop.repository.CustomerRepository;
import com.auca.tailorshop.repository.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final CustomerRepository customerRepository;

    public MeasurementService(MeasurementRepository measurementRepository,
                              CustomerRepository customerRepository) {
        this.measurementRepository = measurementRepository;
        this.customerRepository = customerRepository;
    }

    private Customer loadCustomer(Measurement m) {
        if (m.getCustomer() == null || m.getCustomer().getId() == null) {
            throw new BusinessException("Customer id is required, e.g. \"customer\": {\"id\": 1}");
        }
        Long customerId = m.getCustomer().getId();
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));
    }

    private void checkProportions(Measurement m) {
        // Business rule: waist cannot be larger than chest + 50 cm (sanity check)
        if (m.getWaist() > m.getChest() + 50) {
            throw new BusinessException("Waist looks too large compared to chest; please re-check the values");
        }
    }

    public Measurement create(Measurement m) {
        Customer customer = loadCustomer(m);
        checkProportions(m);
        m.setId(null);
        m.setCustomer(customer);
        return measurementRepository.save(m);
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    public Measurement findById(Long id) {
        return measurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Measurement not found with id " + id));
    }

    public Measurement update(Long id, Measurement updated) {
        Measurement existing = findById(id);
        Customer customer = loadCustomer(updated);
        checkProportions(updated);
        existing.setChest(updated.getChest());
        existing.setWaist(updated.getWaist());
        existing.setHip(updated.getHip());
        existing.setLength(updated.getLength());
        existing.setNotes(updated.getNotes());
        existing.setCustomer(customer);
        return measurementRepository.save(existing);
    }

    public void delete(Long id) {
        measurementRepository.delete(findById(id));
    }
}