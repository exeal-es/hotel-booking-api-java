package com.exeal.hotelbooking.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Hotel {

    String hotelId;

    private Set<String> rooms = new HashSet<>();

    public Hotel(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelId() {
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

    public void addRoom(String roomId) {
        rooms.add(roomId);
    }

    public boolean hasRoom(String roomId) {
        return rooms.contains(roomId);
    }
}
