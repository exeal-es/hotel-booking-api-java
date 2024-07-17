package com.exeal.hotelbooking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class BookingController {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @PostMapping("/bookings")
    public BookingResponse createBooking(@RequestBody BookingRequest bookingRequest) {
        String bookingId = UUID.randomUUID().toString();
        BookingDetailsResponse booking = new BookingDetailsResponse(bookingId, bookingRequest.employeeId(), bookingRequest.roomId(), bookingRequest.startDate(), bookingRequest.endDate());
        bookingRepository.add(booking);
        return new BookingResponse(bookingId, "Reservation confirmed");
    }

    @GetMapping("/bookings/{bookingId}")
    public BookingDetailsResponse getBookingDetails(@PathVariable String bookingId) {
        return bookingRepository.getById(bookingId);
    }
}
