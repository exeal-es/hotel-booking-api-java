package com.exeal.hotelbooking.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Hotel {

  private final HotelId hotelId;

  private final Set<RoomId> rooms = new HashSet<>();

  public Hotel(HotelId hotelId) {
    this.hotelId = hotelId;
  }

  public HotelId getHotelId() {
    return hotelId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Hotel hotel = (Hotel) o;
    return Objects.equals(hotelId, hotel.hotelId) && Objects.equals(rooms, hotel.rooms);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hotelId, rooms);
  }

  public void addRoom(RoomId roomId) {
    rooms.add(roomId);
  }

  public boolean hasRoom(RoomId roomId) {
    return rooms.contains(roomId);
  }
}
