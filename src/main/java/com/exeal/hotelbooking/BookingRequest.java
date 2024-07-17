package com.exeal.hotelbooking;

public record BookingRequest(String employeeId, String roomId, String startDate, String endDate) {
}
