package com.exeal.hotelbooking;

import java.time.LocalDate;

public record BookingRequest(String employeeId, String hotelId, String roomId, String startDate, String endDate) {
    boolean areDatesValid() {
        return startDate().compareTo(endDate()) > 0;
    }

    BookingDates dates() {
        return new BookingDates(LocalDate.parse(startDate()), LocalDate.parse(endDate()));
    }
}
