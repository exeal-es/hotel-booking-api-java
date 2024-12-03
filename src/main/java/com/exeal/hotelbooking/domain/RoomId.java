package com.exeal.hotelbooking.domain;

import java.util.Objects;

public class RoomId {
    private final String value;

    public RoomId(String value) {
        this.value = value;
    }

    public String asString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomId roomId = (RoomId) o;
        return Objects.equals(value, roomId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
