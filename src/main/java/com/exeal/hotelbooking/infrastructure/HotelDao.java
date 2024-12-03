package com.exeal.hotelbooking.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelDao extends JpaRepository<HotelModel, String> {
}
