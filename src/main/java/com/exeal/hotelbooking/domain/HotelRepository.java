package com.exeal.hotelbooking.domain;

import java.util.Optional;

public interface HotelRepository {
    Optional<Hotel> findByHotelId(String hotelId);
}
