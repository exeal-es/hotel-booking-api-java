package com.exeal.hotelbooking;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BookingRepository {
    private final Map<String, BookingDetailsResponse> bookings = new HashMap<>();

    public void add(BookingDetailsResponse booking) {
        bookings.put(booking.bookingId(), booking);
    }

    public BookingDetailsResponse getById(String bookingId) {
        return bookings.get(bookingId);
    }

    public Collection<BookingDetailsResponse> getAll() {
        return bookings.values();
    }
}
