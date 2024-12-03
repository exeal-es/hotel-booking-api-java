package com.exeal.hotelbooking.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Booking {

    private final BookingId bookingId;
    private final String employeeId;
    private final String roomId;
    private final String startDate;
    private final String endDate;
    private final String hotelId;

    public boolean isThereAConflict(String requestedRoomId, BookingDates requestedDates) {
        return roomId.equals(requestedRoomId) && dates().overlapsWith(requestedDates);
    }

    public BookingId getBookingId() {
        return bookingId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Booking(BookingId bookingId, String hotelId, String employeeId, String roomId, String startDate, String endDate) {
        this.bookingId = bookingId;
        this.hotelId = hotelId;
        this.employeeId = employeeId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public BookingDates dates() {
        return new BookingDates(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingId, booking.bookingId) && Objects.equals(employeeId, booking.employeeId) && Objects.equals(roomId, booking.roomId) && Objects.equals(startDate, booking.startDate) && Objects.equals(endDate, booking.endDate) && Objects.equals(hotelId, booking.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, employeeId, roomId, startDate, endDate, hotelId);
    }
}
