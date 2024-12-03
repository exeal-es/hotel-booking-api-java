package com.exeal.hotelbooking.domain;

import java.util.Objects;

public class Booking {

  private final BookingId bookingId;
  private final EmployeeId employeeId;
  private final RoomId roomId;
  private final HotelId hotelId;
  private final BookingDates bookingDates;

  public Booking(
      BookingId bookingId,
      HotelId hotelId,
      EmployeeId employeeId,
      RoomId roomId,
      BookingDates bookingDates) {
    this.bookingId = bookingId;
    this.hotelId = hotelId;
    this.employeeId = employeeId;
    this.roomId = roomId;
    this.bookingDates = bookingDates;
  }

  public boolean isThereAConflict(RoomId requestedRoomId, BookingDates requestedDates) {
    return roomId.equals(requestedRoomId) && bookingDates.overlapsWith(requestedDates);
  }

  public BookingId getBookingId() {
    return bookingId;
  }

  public HotelId getHotelId() {
    return hotelId;
  }

  public EmployeeId getEmployeeId() {
    return employeeId;
  }

  public RoomId getRoomId() {
    return roomId;
  }

  public BookingDates dates() {
    return bookingDates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Booking booking = (Booking) o;
    return Objects.equals(bookingId, booking.bookingId)
        && Objects.equals(employeeId, booking.employeeId)
        && Objects.equals(roomId, booking.roomId)
        && Objects.equals(bookingDates, booking.bookingDates)
        && Objects.equals(hotelId, booking.hotelId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bookingId, employeeId, roomId, bookingDates, hotelId);
  }
}
