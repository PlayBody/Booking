package com.example.booking.payload.response;

import com.example.booking.model.data.DateInterval;

import java.util.List;

public class IntervalsResponse {
    public IntervalsResponse() {

    }
    public IntervalsResponse(List<DateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
    }

    public List<DateInterval> getDateIntervals() {
        return dateIntervals;
    }

    public void setDateIntervals(List<DateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
    }

    List<DateInterval> dateIntervals;

}
