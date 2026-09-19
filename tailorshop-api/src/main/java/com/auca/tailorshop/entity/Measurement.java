package com.auca.tailorshop.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "measurements")
public class Measurement extends BaseEntity {

    @NotNull(message = "Chest is required")
    @DecimalMin(value = "20.0", message = "Chest must be at least 20 cm")
    @DecimalMax(value = "200.0", message = "Chest must be at most 200 cm")
    private Double chest;

    @NotNull(message = "Waist is required")
    @DecimalMin(value = "20.0", message = "Waist must be at least 20 cm")
    @DecimalMax(value = "200.0", message = "Waist must be at most 200 cm")
    private Double waist;

    @DecimalMin(value = "20.0", message = "Hip must be at least 20 cm")
    @DecimalMax(value = "200.0", message = "Hip must be at most 200 cm")
    private Double hip;

    private Double length;

    private String notes;

    @NotNull(message = "Customer is required")
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Double getChest() { return chest; }
    public void setChest(Double chest) { this.chest = chest; }
    public Double getWaist() { return waist; }
    public void setWaist(Double waist) { this.waist = waist; }
    public Double getHip() { return hip; }
    public void setHip(Double hip) { this.hip = hip; }
    public Double getLength() { return length; }
    public void setLength(Double length) { this.length = length; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}