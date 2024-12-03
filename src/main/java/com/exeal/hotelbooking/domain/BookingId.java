package com.exeal.hotelbooking.domain;

import java.util.Objects;
import java.util.UUID;

public class BookingId {
  public static BookingId generate() {
    return new BookingId(UUID.randomUUID());
  }

  public static BookingId fromString(String value) {
    return new BookingId(UUID.fromString(value));
  }

  private final UUID value;

  private BookingId(UUID value) {
    this.value = value;
  }

  public String asString() {
    return value.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BookingId bookingId = (BookingId) o;
    return Objects.equals(value, bookingId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
