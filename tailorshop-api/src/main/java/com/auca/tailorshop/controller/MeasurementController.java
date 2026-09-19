package com.auca.tailorshop.controller;

import com.auca.tailorshop.entity.Measurement;
import com.auca.tailorshop.service.MeasurementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Measurement> create(@Valid @RequestBody Measurement measurement) {
        return new ResponseEntity<>(service.create(measurement), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Measurement> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Measurement getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Measurement update(@PathVariable Long id, @Valid @RequestBody Measurement measurement) {
        return service.update(id, measurement);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        service.delete(id);
        return Collections.singletonMap("message", "Measurement deleted successfully");
    }
}