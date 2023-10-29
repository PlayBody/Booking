package com.example.booking.model;

import com.example.booking.model.data.DateInterval;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @NotNull(message = "Property ID cannot be null.")
    private Long propertyId;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "Start date cannot be null.")
    @FutureOrPresent(message = "Start date must be in present or future.")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "End date cannot be null.")
    @FutureOrPresent(message = "End date must be in present or future.")
    private Date endDate;

    // Default constructor
    public Block() {}

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public DateInterval getInterval() {
        return new DateInterval(startDate, endDate);
    }
}