package com.exeal.hotelbooking.infrastructure;

import com.exeal.hotelbooking.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelDao extends JpaRepository<Hotel, String> {
}
