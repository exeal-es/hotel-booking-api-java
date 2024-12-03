package com.exeal.hotelbooking.domain;

import java.util.Collection;
import java.util.Optional;

public interface BookingRepository {

    Collection<Booking> findAllByHotelId(String hotelId);

    Optional<Booking> findById(String bookingId);

    void save(Booking booking);
}
