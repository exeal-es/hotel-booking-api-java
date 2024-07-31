package com.exeal.hotelbooking;

import java.time.LocalDate;

public record BookingRequest(String employeeId, String hotelId, String roomId, String startDate, String endDate) {
    BookingDates dates() {
        return new BookingDates(LocalDate.parse(startDate()), LocalDate.parse(endDate()));
    }
}
