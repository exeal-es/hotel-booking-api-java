package com.exeal.hotelbooking.infrastructure;

import com.exeal.hotelbooking.domain.Booking;
import com.exeal.hotelbooking.domain.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

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
        booking.getBookingId(),
        booking.getHotelId(),
        booking.getEmployeeId(),
        booking.getRoomId(),
        booking.getStartDate(),
        booking.getEndDate());
  }

  private Booking mapFrom(BookingModel bookingModel) {
    return new Booking(
        bookingModel.getBookingId(),
        bookingModel.getHotelId(),
        bookingModel.getEmployeeId(),
        bookingModel.getRoomId(),
        bookingModel.getStartDate(),
        bookingModel.getEndDate());
  }
}
