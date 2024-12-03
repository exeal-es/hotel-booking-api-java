package com.exeal.hotelbooking.controller;

import com.exeal.hotelbooking.domain.Booking;
import com.exeal.hotelbooking.domain.BookingId;
import com.exeal.hotelbooking.domain.BookingRepository;
import com.exeal.hotelbooking.domain.EmployeeId;
import com.exeal.hotelbooking.domain.Hotel;
import com.exeal.hotelbooking.domain.HotelId;
import com.exeal.hotelbooking.domain.HotelRepository;

import java.util.Collection;
import java.util.Optional;

import com.exeal.hotelbooking.domain.RoomId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;

    public BookingController(BookingRepository bookingRepository, HotelRepository hotelRepository) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
    }

    private static Booking createBookingFrom(BookingRequest bookingRequest) {
        return new Booking(
                BookingId.generate(),
                new HotelId(bookingRequest.hotelId()),
                new EmployeeId(bookingRequest.employeeId()),
                new RoomId(bookingRequest.roomId()),
                bookingRequest.dates()
        );
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        if (bookingRequest.areDatesValid()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Hotel> maybeHotel = hotelRepository.findByHotelId(bookingRequest.hotelId());
        if (maybeHotel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Hotel hotel = maybeHotel.get();
        if (!hotel.hasRoom(bookingRequest.roomId())) {
            return ResponseEntity.badRequest()
                    .body(new ErrorDto("Hotel does not have requested room type"));
        }

        Collection<Booking> allBookingsByHotel = bookingRepository.findAllByHotelId(bookingRequest.hotelId());
        if (allBookingsByHotel.stream().anyMatch(booking -> booking.isThereAConflict(new RoomId(bookingRequest.roomId()), bookingRequest.dates()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Booking booking = createBookingFrom(bookingRequest);
        bookingRepository.save(booking);
        return ResponseEntity.ok(new BookingResponseDto(booking.getBookingId().asString(), "Reservation confirmed"));
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<?> getBookingDetails(@PathVariable String bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Booking booking = optionalBooking.get();
        BookingDto bookingDto = mapFrom(booking);
        return ResponseEntity.ok(bookingDto);
    }

    private BookingDto mapFrom(Booking booking) {
        return new BookingDto(
                booking.getBookingId().asString(),
                booking.getEmployeeId().asString(),
                booking.getRoomId().asString(),
                booking.dates().startDate().toString(),
                booking.dates().endDate().toString(),
                booking.getHotelId().asString()
        );
    }
}
