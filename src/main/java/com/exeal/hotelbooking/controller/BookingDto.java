package com.exeal.hotelbooking.controller;

public record BookingDto(
    String bookingId,
    String employeeId,
    String roomId,
    String startDate,
    String endDate,
    String hotelId) {
}
