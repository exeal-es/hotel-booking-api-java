package com.exeal.hotelbooking;

import java.time.LocalDate;

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
        // Comprobar si el inicio o el fin coinciden exactamente, lo cual no se considera solapamiento
        if (this.startDate.equals(other.endDate) || this.endDate.equals(other.startDate)) {
            return false;
        }
        // Lógica existente para determinar solapamiento
        return !this.startDate.isAfter(other.endDate) && !this.endDate.isBefore(other.startDate);
    }
}
