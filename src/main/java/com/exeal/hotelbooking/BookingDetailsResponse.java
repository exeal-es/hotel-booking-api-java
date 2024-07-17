package com.exeal.hotelbooking;

import java.time.LocalDate;

public record BookingDetailsResponse(String bookingId, String employeeId, String roomId, String startDate, String endDate) {
    BookingDates dates() {
        return new BookingDates(LocalDate.parse(startDate()), LocalDate.parse(endDate()));
    }
}
