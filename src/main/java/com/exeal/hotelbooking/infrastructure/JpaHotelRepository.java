package com.exeal.hotelbooking.infrastructure;

import com.exeal.hotelbooking.domain.Hotel;
import com.exeal.hotelbooking.domain.HotelId;
import com.exeal.hotelbooking.domain.HotelRepository;
import com.exeal.hotelbooking.domain.RoomId;
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
    Hotel hotel = new Hotel(new HotelId(hotelModel.getHotelId()));
    for (String roomId : hotelModel.getRooms()) {
      hotel.addRoom(new RoomId(roomId));
    }
    return hotel;
  }
}
