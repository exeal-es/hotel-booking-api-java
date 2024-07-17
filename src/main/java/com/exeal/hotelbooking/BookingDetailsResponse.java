package com.exeal.hotelbooking;

public record BookingDetailsResponse(String bookingId, String employeeId, String roomId, String startDate, String endDate) {
}
