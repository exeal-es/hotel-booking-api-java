package com.exeal.hotelbooking;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookingDatesTest {

    @Test
    void testBookingDurationLessThanOneDayThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BookingDates(LocalDate.parse("2023-04-07"), LocalDate.parse("2023-04-07"));
        });
    }

    @Test
    void testNoOverlapBeforeExistingBooking() {
        BookingDates dates = new BookingDates(LocalDate.parse("2023-04-07"), LocalDate.parse("2023-04-10"));
        BookingDates other = new BookingDates(LocalDate.parse("2023-04-01"), LocalDate.parse("2023-04-05"));
        assertFalse(dates.overlapsWith(other));
    }

    @Test
    void testNoOverlapAfterExistingBooking() {
        BookingDates dates = new BookingDates(LocalDate.parse("2023-04-01"), LocalDate.parse("2023-04-05"));
        BookingDates other = new BookingDates(LocalDate.parse("2023-04-07"), LocalDate.parse("2023-04-10"));
        assertFalse(dates.overlapsWith(other));
    }

    @Test
    void testOverlapStartsBeforeEndsDuringExistingBooking() {
        BookingDates dates = new BookingDates(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08"));
        BookingDates other = new BookingDates(LocalDate.parse("2023-04-01"), LocalDate.parse("2023-04-04"));
        assertTrue(dates.overlapsWith(other));
    }

    @Test
    void testOverlapStartsBeforeAndEndsAfterExistingBooking() {
        BookingDates dates = new BookingDates(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08"));
        BookingDates other = new BookingDates(LocalDate.parse("2023-04-02"), LocalDate.parse("2023-04-09"));
        assertTrue(dates.overlapsWith(other));
    }

    @Test
    void testOverlapStartsDuringEndsAfterExistingBooking() {
        BookingDates dates = new BookingDates(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08"));
        BookingDates other = new BookingDates(LocalDate.parse("2023-04-05"), LocalDate.parse("2023-04-10"));
        assertTrue(dates.overlapsWith(other));
    }

    @Test
    void testOverlapStartsAndEndsOnTheSameDayAsExistingBooking() {
        BookingDates dates = new BookingDates(LocalDate.parse("2023-04-04"), LocalDate.parse("2023-04-06"));
        assertTrue(dates.overlapsWith(dates));
    }

    @Test
    void testNoOverlapWhenStartDateEqualsEndDateOfExistingBooking() {
        BookingDates dates = new BookingDates(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08"));
        BookingDates other = new BookingDates(LocalDate.parse("2023-04-08"), LocalDate.parse("2023-04-10"));
        assertFalse(dates.overlapsWith(other));
    }

    @Test
    void testNoOverlapWhenEndDateEqualsStartDateOfExistingBooking() {
        BookingDates dates = new BookingDates(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08"));
        BookingDates other = new BookingDates(LocalDate.parse("2023-04-01"), LocalDate.parse("2023-04-03"));
        assertFalse(dates.overlapsWith(other));
    }
}
