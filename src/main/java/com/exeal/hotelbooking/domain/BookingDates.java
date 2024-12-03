package com.exeal.hotelbooking.domain;

import java.time.LocalDate;
import java.util.Objects;

public class BookingDates {
  private final LocalDate startDate;
  private final LocalDate endDate;

  public BookingDates(LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate) || startDate.equals(endDate)) {
      throw new IllegalArgumentException("Booking duration must be at least one day");
    }

    this.startDate = startDate;
    this.endDate = endDate;
  }

  public boolean overlapsWith(BookingDates other) {
    if (this.startDate.equals(other.endDate) || this.endDate.equals(other.startDate)) {
      return false;
    }
    return !this.startDate.isAfter(other.endDate) && !this.endDate.isBefore(other.startDate);
  }

  public LocalDate startDate() {
    return startDate;
  }

  public LocalDate endDate() {
    return endDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BookingDates that = (BookingDates) o;
    return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDate, endDate);
  }
}
