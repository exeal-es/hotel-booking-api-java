package com.exeal.hotelbooking;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    @PostMapping("/bookings")
    public BookingResponse createBooking(@RequestBody BookingRequest bookingRequest) {
        return new BookingResponse("1", "Reservation confirmed");
    }
}
