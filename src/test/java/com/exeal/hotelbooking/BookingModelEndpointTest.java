package com.exeal.hotelbooking;

import com.exeal.hotelbooking.domain.HotelModel;
import com.exeal.hotelbooking.infrastructure.BookingDao;
import com.exeal.hotelbooking.infrastructure.HotelDao;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingModelEndpointTest {

    @Resource
    BookingDao bookingDao;

    @Resource
    HotelDao hotelRepository;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        bookingDao.deleteAll();
    }

    @Test
    void bookingRoomReturnsConfirmation() {
        // Given
        String hotelId = givenAHotelWithASingleRoom("101");

        // When
        Response response = bookRoom(hotelId, "101", "2023-04-01", "2023-04-05");

        // Then
        assertBookingConfirmationIsReturned(response);
    }

    private static void assertBookingConfirmationIsReturned(Response response) {
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body("bookingId", notNullValue())
            .body("message", equalTo("Reservation confirmed"));
    }

    private String givenAHotelWithASingleRoom(String roomId) {
        String hotelId = UUID.randomUUID().toString();
        HotelModel hotelModel = new HotelModel(hotelId);
        hotelModel.addRoom(roomId);
        hotelRepository.save(hotelModel);
        return hotelId;
    }

    private static Response bookRoom(String hotelId, String roomId, String startDate, String endDate) {
        String requestBody = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "%s",
                "startDate": "%s",
                "endDate": "%s"
            }
            """, hotelId, roomId, startDate, endDate);

        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bookings");
    }

    @Test
    void bookingAndRetrieveDetailsTest() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        HotelModel hotelModel = new HotelModel(hotelId);
        hotelModel.addRoom("101");
        hotelRepository.save(hotelModel);

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
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("bookingId");

        // When
        Response response = given()
                .pathParam("bookingId", bookingId)
                .when()
                .get("/bookings/{bookingId}");

        // Then
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("employeeId", equalTo("123"))
                .body("hotelId", equalTo(hotelId))
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
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void bookingWithStartDateAfterEndDateReturnsBadRequest() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        HotelModel hotelModel = new HotelModel(hotelId);
        hotelModel.addRoom("101");
        hotelRepository.save(hotelModel);

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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void bookingWithEndDateBetweenAnotherBookingDatesReturnsConflict() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        HotelModel hotelModel = new HotelModel(hotelId);
        hotelModel.addRoom("101");
        hotelRepository.save(hotelModel);

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
                .statusCode(HttpStatus.OK.value());
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
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void bookingWithConflictingDatesButDifferentHotelsAreAllowed() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        HotelModel hotelModel = new HotelModel(hotelId);
        hotelModel.addRoom("101");
        hotelRepository.save(hotelModel);

        String hotelId2 = UUID.randomUUID().toString();
        HotelModel hotelModel2 = new HotelModel(hotelId2);
        hotelModel2.addRoom("101");
        hotelRepository.save(hotelModel2);

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
                .statusCode(HttpStatus.OK.value());
        // When
        String requestBodyR2 = String.format("""
            {
                "employeeId": "123",
                "hotelId": "%s",
                "roomId": "101",
                "startDate": "2023-03-31",
                "endDate": "2023-04-03"
            }
            """, hotelId2);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBodyR2)
                .when()
                .post("/bookings");

        // Then
        response.then()
                .statusCode(HttpStatus.OK.value());
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
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void canBookDifferentTypesOfRoomEvenWithinTheSameDates() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        HotelModel hotelModel = new HotelModel(hotelId);
        hotelModel.addRoom("101");
        hotelModel.addRoom("102");
        hotelRepository.save(hotelModel);

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
                .statusCode(HttpStatus.OK.value());

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
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void ifHotelDoesNotHaveRoomTypeRequestedThenReturn400() {
        // Given
        String hotelId = UUID.randomUUID().toString();
        hotelRepository.save(new HotelModel(hotelId));

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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("Hotel does not have requested room type"));
    }
}
