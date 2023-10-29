package com.example.booking.util;

import com.example.booking.model.data.DateInterval;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logic {
    public static boolean isOverlappedDateInterval(DateInterval a, DateInterval b) {
        return a.getEnd().after(b.getStart()) && a.getStart().before(b.getEnd());
    }

    public static void sortBookingByStartDate(List<DateInterval> DateInterval) {
        DateInterval.sort((a, b) -> {
            if(a.getStart().equals(b.getStart())){
                if(a.getEnd().equals(b.getEnd())){
                    return 0;
                }
                return a.getEnd().before(b.getEnd()) ? -1 : 1;
            }
            return a.getStart().before(b.getStart()) ? -1 : 1;
        });
    }

    public static List<DateInterval> getSplitIntervals(List<DateInterval> intervals, DateInterval checkInterval) {
        List<DateInterval> outIntervals = new ArrayList<>();
        Logic.sortBookingByStartDate(intervals);
        int i;
        for (i=0; i<intervals.size(); i++){
            DateInterval interval = intervals.get(i);
            if(interval.isContain(checkInterval.getStart())){
                checkInterval.setStart(interval.getEnd());
            } else if(interval.isContain(checkInterval.getEnd())){
                checkInterval.setEnd(interval.getStart());
                break;
            } else if(checkInterval.isContain(interval.getStart()) && checkInterval.isContain(interval.getEnd())){
                if(checkInterval.getStart().before(interval.getStart())){
                    outIntervals.add(new DateInterval(checkInterval.getStart(), interval.getStart()));
                }
                checkInterval.setStart(interval.getEnd());
            }
            if(!checkInterval.isAvailable()) {
                break;
            }
        }
        if(checkInterval.isAvailable()){
            outIntervals.add(new DateInterval(checkInterval));
        }
        return outIntervals;
    }

    public static Date convertString2Date(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(dateString);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
