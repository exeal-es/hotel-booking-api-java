package com.exeal.hotelbooking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@RestController
public class BookingController {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        if (bookingRequest.startDate().compareTo(bookingRequest.endDate()) > 0) {
            return ResponseEntity.badRequest().build();
        }

        Collection<BookingDetailsResponse> allBookings = bookingRepository.getAll();
        if (allBookings.stream().anyMatch(booking -> new BookingDates(LocalDate.parse(booking.startDate()), LocalDate.parse(booking.endDate())).overlapsWith(new BookingDates(LocalDate.parse(bookingRequest.startDate()), LocalDate.parse(bookingRequest.endDate()))))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String bookingId = UUID.randomUUID().toString();
        BookingDetailsResponse booking = new BookingDetailsResponse(bookingId, bookingRequest.employeeId(), bookingRequest.roomId(), bookingRequest.startDate(), bookingRequest.endDate());
        bookingRepository.add(booking);
        return ResponseEntity.ok(new BookingResponse(bookingId, "Reservation confirmed"));
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<?> getBookingDetails(@PathVariable String bookingId) {
        BookingDetailsResponse booking = bookingRepository.getById(bookingId);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }
}
