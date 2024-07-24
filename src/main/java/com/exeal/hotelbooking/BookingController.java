package com.exeal.hotelbooking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
public class BookingController {

    private final BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        if (bookingRequest.startDate().compareTo(bookingRequest.endDate()) > 0) {
            return ResponseEntity.badRequest().build();
        }

        Collection<BookingDetailsResponse> allBookings = bookingRepository.findAll();
        if (allBookings.stream().anyMatch(booking -> booking.dates().overlapsWith(bookingRequest.dates()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String bookingId = UUID.randomUUID().toString();
        BookingDetailsResponse booking = new BookingDetailsResponse(
                bookingId,
                bookingRequest.employeeId(),
                bookingRequest.roomId(),
                bookingRequest.startDate(),
                bookingRequest.endDate()
        );
        bookingRepository.save(booking);
        return ResponseEntity.ok(new BookingResponse(bookingId, "Reservation confirmed"));
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<?> getBookingDetails(@PathVariable String bookingId) {
        Optional<BookingDetailsResponse> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking.get());
    }
}
