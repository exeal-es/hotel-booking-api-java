package com.exeal.hotelbooking.controller;

public record BookingRequest(
    String employeeId, String hotelId, String roomId, String startDate, String endDate) {}
