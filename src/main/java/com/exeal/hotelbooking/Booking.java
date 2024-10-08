package com.exeal.hotelbooking;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Objects;

// TODO: Try to convert this to record and make it work with JPA.
@Entity
public class Booking {

    @Id String bookingId;
    String employeeId;
    String roomId;
    String startDate;
    String endDate;
    String hotelId;

    public boolean isThereAConflict(String requestedRoomId, BookingDates requestedDates) {
        return roomId.equals(requestedRoomId) && dates().overlapsWith(requestedDates);
    }

    public String getBookingId() {
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

    public Booking() {
    }

    public Booking(String bookingId, String hotelId, String employeeId, String roomId, String startDate, String endDate) {
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
        Booking that = (Booking) o;
        return Objects.equals(bookingId, that.bookingId) && Objects.equals(employeeId, that.employeeId) && Objects.equals(roomId, that.roomId) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, employeeId, roomId, startDate, endDate);
    }
}
