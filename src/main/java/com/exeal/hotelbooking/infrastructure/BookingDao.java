package com.exeal.hotelbooking.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookingDao extends JpaRepository<BookingModel, String> {
    Collection<BookingModel> findAllByHotelId(String hotelId);
}
