package com.exeal.hotelbooking.infrastructure;

import com.exeal.hotelbooking.domain.Hotel;
import com.exeal.hotelbooking.domain.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaHotelRepository implements HotelRepository {

  private final HotelDao hotelDao;

  public JpaHotelRepository(HotelDao hotelDao) {
    this.hotelDao = hotelDao;
  }

  @Override
  public Optional<Hotel> findByHotelId(String hotelId) {
    return hotelDao.findById(hotelId).map(this::mapTo);
  }

  private Hotel mapTo(HotelModel hotelModel) {
    Hotel hotel = new Hotel(hotelModel.getHotelId());
    for (String roomId : hotelModel.getRooms()) {
      hotel.addRoom(roomId);
    }
    return hotel;
  }
}
