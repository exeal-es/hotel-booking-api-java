package com.exeal.hotelbooking.infrastructure;

import com.exeal.hotelbooking.domain.Booking;
import com.exeal.hotelbooking.domain.BookingId;
import com.exeal.hotelbooking.domain.BookingRepository;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.exeal.hotelbooking.domain.EmployeeId;
import com.exeal.hotelbooking.domain.HotelId;
import com.exeal.hotelbooking.domain.RoomId;
import org.springframework.stereotype.Service;

@Service
public class JpaBookingRepository implements BookingRepository {

  private final BookingDao bookingDao;

  public JpaBookingRepository(BookingDao bookingDao) {
    this.bookingDao = bookingDao;
  }

  @Override
  public Collection<Booking> findAllByHotelId(String hotelId) {
    return bookingDao.findAllByHotelId(hotelId).stream()
        .map(this::mapFrom)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Booking> findById(String bookingId) {
    return bookingDao.findById(bookingId).map(this::mapFrom);
  }

  @Override
  public void save(Booking booking) {
    bookingDao.save(mapTo(booking));
  }

  private BookingModel mapTo(Booking booking) {
    return new BookingModel(
        booking.getBookingId().asString(),
        booking.getHotelId().asString(),
        booking.getEmployeeId().asString(),
        booking.getRoomId().asString(),
        booking.getStartDate(),
        booking.getEndDate());
  }

  private Booking mapFrom(BookingModel bookingModel) {
    return new Booking(
        BookingId.fromString(bookingModel.getBookingId()),
        new HotelId(bookingModel.getHotelId()),
        new EmployeeId(bookingModel.getEmployeeId()),
        new RoomId(bookingModel.getRoomId()),
        bookingModel.getStartDate(),
        bookingModel.getEndDate());
  }
}
