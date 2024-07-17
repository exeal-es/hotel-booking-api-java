package com.exeal.hotelbooking;

import org.springframework.web.bind.annotation.*;

@RestController
public class BookingController {

    @PostMapping("/bookings")
    public BookingResponse createBooking(@RequestBody BookingRequest bookingRequest) {
        return new BookingResponse("1", "Reservation confirmed");
    }

    @GetMapping("/bookings/{bookingId}")
    public BookingDetailsResponse getBookingDetails(@PathVariable String bookingId) {
        return new BookingDetailsResponse(bookingId, "123", "101", "2023-04-01", "2023-04-05");
    }
}
