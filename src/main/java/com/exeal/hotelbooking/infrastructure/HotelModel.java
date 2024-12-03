package com.exeal.hotelbooking.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
public class HotelModel {

  @Id String hotelId;

  private Set<String> rooms = new HashSet<>();

  public HotelModel(String hotelId) {
    this.hotelId = hotelId;
  }

  public HotelModel() {}

  public String getHotelId() {
    return hotelId;
  }

  public void addRoom(String roomId) {
    rooms.add(roomId);
  }

  public Set<String> getRooms() {
    return rooms;
  }
}
