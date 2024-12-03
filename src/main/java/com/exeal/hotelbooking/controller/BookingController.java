package com.exeal.hotelbooking.controller;

import com.exeal.hotelbooking.infrastructure.BookingModel;
import com.exeal.hotelbooking.infrastructure.BookingDao;
import com.exeal.hotelbooking.infrastructure.HotelModel;
import com.exeal.hotelbooking.infrastructure.HotelDao;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    private final BookingDao bookingRepository;
    private final HotelDao hotelDao;

    public BookingController(BookingDao bookingDao, HotelDao hotelDao) {
        this.bookingRepository = bookingDao;
        this.hotelDao = hotelDao;
    }

    private static BookingModel createBookingFrom(BookingRequest bookingRequest) {
        String bookingId = UUID.randomUUID().toString();
        return new BookingModel(
                bookingId,
                bookingRequest.hotelId(),
                bookingRequest.employeeId(),
                bookingRequest.roomId(),
                bookingRequest.startDate(),
                bookingRequest.endDate()
        );
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        if (bookingRequest.areDatesValid()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<HotelModel> maybeHotel = hotelDao.findById(bookingRequest.hotelId());
        if (maybeHotel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HotelModel hotelModel = maybeHotel.get();
        if (!hotelModel.hasRoom(bookingRequest.roomId())) {
            return ResponseEntity.badRequest()
                    .body(new ErrorDto("Hotel does not have requested room type"));
        }

        Collection<BookingModel> allBookingsByHotel = bookingRepository.findAllByHotelId(bookingRequest.hotelId());
        if (allBookingsByHotel.stream().anyMatch(bookingModel -> bookingModel.isThereAConflict(bookingRequest.roomId(), bookingRequest.dates()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        BookingModel bookingModel = createBookingFrom(bookingRequest);
        bookingRepository.save(bookingModel);
        return ResponseEntity.ok(new BookingResponse(bookingModel.getBookingId(), "Reservation confirmed"));
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<?> getBookingDetails(@PathVariable String bookingId) {
        Optional<BookingModel> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        BookingModel bookingModel = optionalBooking.get();
        BookingDto bookingDto = mapFrom(bookingModel);
        return ResponseEntity.ok(bookingDto);
    }

    private BookingDto mapFrom(BookingModel bookingModel) {
        return new BookingDto(
                bookingModel.getBookingId(),
                bookingModel.getEmployeeId(),
                bookingModel.getRoomId(),
                bookingModel.getStartDate(),
                bookingModel.getEndDate(),
                bookingModel.getHotelId()
        );
    }
}
