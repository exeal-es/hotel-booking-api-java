package com.exeal.hotelbooking.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    Collection<Booking> findAllByHotelId(String hotelId);
}
