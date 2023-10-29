package com.example.booking.model.data;

import java.util.Date;

public class DateInterval {
    private Date start;
    private Date end;

    public DateInterval(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
    }

    public DateInterval(DateInterval other) {
        this.start = new Date(other.start.getTime());
        this.end = new Date(other.end.getTime());
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean isContain(Date other){
        return (!start.after(other)) && (!end.before(other));
    }

    public boolean isAvailable() {
        return end.after(start);
    }
}
