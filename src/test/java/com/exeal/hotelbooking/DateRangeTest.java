package com.exeal.hotelbooking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateRangeTest {

    @Test
    void testNoOverlapBeforeExistingBooking() {
        assertFalse(new DateRange("2023-04-07", "2023-04-10").overlapsWith(new DateRange("2023-04-01", "2023-04-05")));
    }

    @Test
    void testNoOverlapAfterExistingBooking() {
        assertFalse(new DateRange("2023-04-01", "2023-04-05").overlapsWith(new DateRange("2023-04-07", "2023-04-10")));
    }

    @Test
    void testOverlapStartsBeforeEndsDuringExistingBooking() {
        assertTrue(new DateRange("2023-04-03", "2023-04-08").overlapsWith(new DateRange("2023-04-01", "2023-04-04")));
    }

    @Test
    void testOverlapStartsBeforeAndEndsAfterExistingBooking() {
        assertTrue(new DateRange("2023-04-03", "2023-04-08").overlapsWith(new DateRange("2023-04-02", "2023-04-09")));
    }

    @Test
    void testOverlapStartsDuringEndsAfterExistingBooking() {
        assertTrue(new DateRange("2023-04-03", "2023-04-08").overlapsWith(new DateRange("2023-04-05", "2023-04-10")));
    }

    @Test
    void testOverlapStartsAndEndsOnTheSameDayAsExistingBooking() {
        assertTrue(new DateRange("2023-04-04", "2023-04-06").overlapsWith(new DateRange("2023-04-05", "2023-04-05")));
    }
}