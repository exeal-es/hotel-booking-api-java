package com.exeal.hotelbooking;

public class DateRange {
    private final String startDate;
    private final String endDate;

    public DateRange(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean overlapsWith(DateRange other) {
        return startDate.compareTo(other.endDate) < 0 && other.startDate.compareTo(startDate) < 0;
    }
}
