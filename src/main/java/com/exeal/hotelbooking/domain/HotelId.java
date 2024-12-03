package com.exeal.hotelbooking.domain;

import java.util.Objects;

public class HotelId {
    private final String value;

    public HotelId(String value) {
        this.value = value;
    }

    public String asString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelId hotelId = (HotelId) o;
        return Objects.equals(value, hotelId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
