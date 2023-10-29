package com.example.booking.model;

import com.example.booking.model.data.DateInterval;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
public class Booking {

    public enum BookingStatus {
        Enable,
        Cancel,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Property ID cannot be null.")
    private Long propertyId;

    @NotBlank(message = "Guest name cannot be blank.")
    private String guestName;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "Start date cannot be null.")
    @FutureOrPresent(message = "Start date must be in present or future.")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "End date cannot be null.")
    @FutureOrPresent(message = "End date must be in present or future.")
    private Date endDate;

    private String guestData;

    private BookingStatus status;

    public Booking() {
        status = BookingStatus.Enable;
    }

    public Long getId() {
        if(id == null) {
            return -1L;
        }
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

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
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

    public String getGuestData() {
        return guestData;
    }

    public void setGuestData(String guestData) {
        this.guestData = guestData;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public boolean isEnable() {
        return this.status == BookingStatus.Enable;
    }

    public void setCancel() {
        this.status = BookingStatus.Cancel;
    }

    public DateInterval getInterval() {
        return new DateInterval(startDate, endDate);
    }
}