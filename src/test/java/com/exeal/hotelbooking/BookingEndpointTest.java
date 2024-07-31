package com.exeal.hotelbooking;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingEndpointTest {

    @Resource
    BookingRepository bookingRepository;

    @Resource
    HotelRepository hotelRepository;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        bookingRepository.deleteAll();
    }

    @Test
    void bookingRoomReturnsConfirmation() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        Hotel hotel = new Hotel(hotelId);
        hotel.addRoom("101");
        hotelRepository.save(hotel);

        // When
        String requestBody = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "101",
                "startDate": "2023-04-01",
                "endDate": "2023-04-05"
            }
            """, hotelId);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bookings");

        // Then
        response.then()
            .statusCode(200)
            .body("bookingId", notNullValue())
            .body("message", equalTo("Reservation confirmed"));
    }

    @Test
    void bookingAndRetrieveDetailsTest() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        Hotel hotel = new Hotel(hotelId);
        hotel.addRoom("101");
        hotelRepository.save(hotel);

        String requestBody = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "101",
                "startDate": "2023-04-01",
                "endDate": "2023-04-05"
            }
            """, hotelId);

        String bookingId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bookings")
                .then()
                .statusCode(200)
                .extract()
                .path("bookingId");

        // When
        Response response = given()
                .pathParam("bookingId", bookingId)
                .when()
                .get("/bookings/{bookingId}");

        // Then
        response.then()
                .statusCode(200)
                .body("employeeId", equalTo("123"))
                .body("roomId", equalTo("101"))
                .body("startDate", equalTo("2023-04-01"))
                .body("endDate", equalTo("2023-04-05"));
    }

    @Test
    void getNonExistentBookingDetailsReturnsNotFound() {
        // Given
        String nonExistentBookingId = "non-existent-id";

        // When
        Response response = given()
                .pathParam("bookingId", nonExistentBookingId)
                .when()
                .get("/bookings/{bookingId}");

        // Then
        response.then()
                .statusCode(404);
    }

    @Test
    void bookingWithStartDateAfterEndDateReturnsBadRequest() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        Hotel hotel = new Hotel(hotelId);
        hotel.addRoom("101");
        hotelRepository.save(hotel);

        // When
        String requestBody = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "101",
                "startDate": "2023-04-06",
                "endDate": "2023-04-05"
            }
            """, hotelId);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bookings");

        // Then
        response.then()
                .statusCode(400);
    }

    @Test
    void bookingWithEndDateBetweenAnotherBookingDatesReturnsConflict() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        Hotel hotel = new Hotel(hotelId);
        hotel.addRoom("101");
        hotelRepository.save(hotel);

        String requestBodyR1 = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "101",
                "startDate": "2023-04-01",
                "endDate": "2023-04-05"
            }
            """, hotelId);

        given()
                .contentType(ContentType.JSON)
                .body(requestBodyR1)
                .when()
                .post("/bookings")
                .then()
                .statusCode(200);
        // When
        String requestBodyR2 = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "101",
                "startDate": "2023-03-31",
                "endDate": "2023-04-03"
            }
            """, hotelId);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBodyR2)
                .when()
                .post("/bookings");

        // Then
        response.then()
                .statusCode(409);
    }

    @Test
    void ifHotelDoesNotExistThenReturn404() {
        // Given
        String nonExistentHotelId = UUID.randomUUID().toString();

        // When
        String requestBody = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "101",
                "startDate": "2023-04-01",
                "endDate": "2023-04-05"
            }
            """, nonExistentHotelId);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bookings");

        // Then
        response.then()
                .statusCode(404);
    }

    @Test
    void canBookDifferentTypesOfRoomEvenWithinTheSameDates() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        Hotel hotel = new Hotel(hotelId);
        hotel.addRoom("101");
        hotel.addRoom("102");
        hotelRepository.save(hotel);

        // When
        String requestBodyR1 = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "101",
                "startDate": "2023-04-01",
                "endDate": "2023-04-05"
            }
            """, hotelId);

        given()
                .contentType(ContentType.JSON)
                .body(requestBodyR1)
                .when()
                .post("/bookings")
                .then()
                .statusCode(200);

        String requestBodyR2 = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "102",
                "startDate": "2023-03-01",
                "endDate": "2023-04-05"
            }
            """, hotelId);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBodyR2)
                .when()
                .post("/bookings");

        // Then
        response.then()
                .statusCode(200);
    }

    @Test
    void ifHotelDoesNotHaveRoomTypeRequestedThenReturn400() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        hotelRepository.save(new Hotel(hotelId));

        // When
        String requestBody = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "101",
                "startDate": "2023-04-01",
                "endDate": "2023-04-05"
            }
            """, hotelId);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bookings");

        // Then
        response.then()
                .statusCode(400)
                .body("message", equalTo("Hotel does not have requested room type"));
    }
}
