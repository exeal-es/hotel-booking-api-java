package com.exeal.hotelbooking;

import com.exeal.hotelbooking.domain.BookingDates;
import com.exeal.hotelbooking.domain.Result;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingModelDatesTest {

    @Test
    void testBookingDurationLessThanOneDayFails() {
        Result<BookingDates> datesResult = BookingDates.create(LocalDate.parse("2023-04-07"), LocalDate.parse("2023-04-07"));

        assertFalse(datesResult.isValid());
    }

    @Test
    void testNoOverlapBeforeExistingBooking() {
        BookingDates dates = BookingDates.create(LocalDate.parse("2023-04-07"), LocalDate.parse("2023-04-10")).get();
        BookingDates other = BookingDates.create(LocalDate.parse("2023-04-01"), LocalDate.parse("2023-04-05")).get();
        assertFalse(dates.overlapsWith(other));
    }

    @Test
    void testNoOverlapAfterExistingBooking() {
        BookingDates dates = BookingDates.create(LocalDate.parse("2023-04-01"), LocalDate.parse("2023-04-05")).get();
        BookingDates other = BookingDates.create(LocalDate.parse("2023-04-07"), LocalDate.parse("2023-04-10")).get();
        assertFalse(dates.overlapsWith(other));
    }

    @Test
    void testOverlapStartsBeforeEndsDuringExistingBooking() {
        BookingDates dates = BookingDates.create(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08")).get();
        BookingDates other = BookingDates.create(LocalDate.parse("2023-04-01"), LocalDate.parse("2023-04-04")).get();
        assertTrue(dates.overlapsWith(other));
    }

    @Test
    void testOverlapStartsBeforeAndEndsAfterExistingBooking() {
        BookingDates dates = BookingDates.create(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08")).get();
        BookingDates other = BookingDates.create(LocalDate.parse("2023-04-02"), LocalDate.parse("2023-04-09")).get();
        assertTrue(dates.overlapsWith(other));
    }

    @Test
    void testOverlapStartsDuringEndsAfterExistingBooking() {
        BookingDates dates = BookingDates.create(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08")).get();
        BookingDates other = BookingDates.create(LocalDate.parse("2023-04-05"), LocalDate.parse("2023-04-10")).get();
        assertTrue(dates.overlapsWith(other));
    }

    @Test
    void testOverlapStartsAndEndsOnTheSameDayAsExistingBooking() {
        BookingDates dates = BookingDates.create(LocalDate.parse("2023-04-04"), LocalDate.parse("2023-04-06")).get();
        assertTrue(dates.overlapsWith(dates));
    }

    @Test
    void testNoOverlapWhenStartDateEqualsEndDateOfExistingBooking() {
        BookingDates dates = BookingDates.create(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08")).get();
        BookingDates other = BookingDates.create(LocalDate.parse("2023-04-08"), LocalDate.parse("2023-04-10")).get();
        assertFalse(dates.overlapsWith(other));
    }

    @Test
    void testNoOverlapWhenEndDateEqualsStartDateOfExistingBooking() {
        BookingDates dates = BookingDates.create(LocalDate.parse("2023-04-03"), LocalDate.parse("2023-04-08")).get();
        BookingDates other = BookingDates.create(LocalDate.parse("2023-04-01"), LocalDate.parse("2023-04-03")).get();
        assertFalse(dates.overlapsWith(other));
    }
}
