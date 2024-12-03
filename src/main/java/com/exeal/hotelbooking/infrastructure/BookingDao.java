package com.exeal.hotelbooking.infrastructure;

import com.exeal.hotelbooking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookingDao extends JpaRepository<Booking, String> {
    Collection<Booking> findAllByHotelId(String hotelId);
}
