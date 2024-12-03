package com.exeal.hotelbooking.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class HotelModel {

    @Id
    String hotelId;

    private Set<String> rooms = new HashSet<>();

    public HotelModel(String hotelId) {
        this.hotelId = hotelId;
    }

    public HotelModel() {
    }

    public String getHotelId() {
        return hotelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelModel hotelModel = (HotelModel) o;
        return Objects.equals(hotelId, hotelModel.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hotelId);
    }

    public void addRoom(String roomId) {
        rooms.add(roomId);
    }

    public boolean hasRoom(String roomId) {
        return rooms.contains(roomId);
    }
}
