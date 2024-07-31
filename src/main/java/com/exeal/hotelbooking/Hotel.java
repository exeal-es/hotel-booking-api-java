package com.exeal.hotelbooking;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Hotel {

    @Id
    String hotelId;

    public Hotel(String hotelId) {
        this.hotelId = hotelId;
    }

    public Hotel() {
    }

    public String getHotelId() {
        return hotelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(hotelId, hotel.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hotelId);
    }
}
