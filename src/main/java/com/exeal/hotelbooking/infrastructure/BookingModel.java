package com.exeal.hotelbooking.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// TODO: Try to convert this to record and make it work with JPA.
@Entity
public class BookingModel {

  @Id String bookingId;
  String employeeId;
  String roomId;
  String startDate;
  String endDate;
  String hotelId;

  public BookingModel() {}

  public BookingModel(
      String bookingId,
      String hotelId,
      String employeeId,
      String roomId,
      String startDate,
      String endDate) {
    this.bookingId = bookingId;
    this.hotelId = hotelId;
    this.employeeId = employeeId;
    this.roomId = roomId;
    this.startDate = startDate;
    this.endDate = endDate;
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
}
