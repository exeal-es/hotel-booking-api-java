package com.exeal.hotelbooking;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingEndpointTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void bookingRoomReturnsConfirmation() {
        String requestBody = """
                {
                    "employeeId": "123",
                    "roomId": "101",
                    "startDate": "2023-04-01",
                    "endDate": "2023-04-05"
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/bookings")
        .then()
            .statusCode(200)
            .body("bookingId", notNullValue())
            .body("message", equalTo("Reservation confirmed"));
    }
}