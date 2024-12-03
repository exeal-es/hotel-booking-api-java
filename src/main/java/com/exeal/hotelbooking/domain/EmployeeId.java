package com.exeal.hotelbooking.domain;

import java.util.Objects;

public class EmployeeId {
    private final String value;

    public EmployeeId(String value) {
        this.value = value;
    }

    public String asString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeId that = (EmployeeId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
