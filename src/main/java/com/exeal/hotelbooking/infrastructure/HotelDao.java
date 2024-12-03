package com.exeal.hotelbooking.infrastructure;

import com.exeal.hotelbooking.domain.HotelModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelDao extends JpaRepository<HotelModel, String> {
}
