package com.exeal.hotelbooking;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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

    @Test
    void bookingAndRetrieveDetailsTest() {
        String requestBody = """
            {
                "employeeId": "123",
                "roomId": "101",
                "startDate": "2023-04-01",
                "endDate": "2023-04-05"
            }
            """;

        String bookingId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bookings")
                .then()
                .statusCode(200)
                .extract()
                .path("bookingId");

        given()
                .pathParam("bookingId", bookingId)
                .when()
                .get("/bookings/{bookingId}")
                .then()
                .statusCode(200)
                .body("employeeId", equalTo("123"))
                .body("roomId", equalTo("101"))
                .body("startDate", equalTo("2023-04-01"))
                .body("endDate", equalTo("2023-04-05"));
    }

    @Test
    void getNonExistentBookingDetailsReturnsNotFound() {
        String nonExistentBookingId = "non-existent-id";

        given()
                .pathParam("bookingId", nonExistentBookingId)
                .when()
                .get("/bookings/{bookingId}")
                .then()
                .statusCode(404);
    }

    @Test
    void bookingWithStartDateAfterEndDateReturnsBadRequest() {
        String requestBody = """
            {
                "employeeId": "123",
                "roomId": "101",
                "startDate": "2023-04-06",
                "endDate": "2023-04-05"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bookings")
                .then()
                .statusCode(400);
    }

    @Test
    void bookingWithEndDateBetweenAnotherBookingDatesReturnsConflict() {
        String requestBodyR1 = """
            {
                "employeeId": "123",
                "roomId": "101",
                "startDate": "2023-04-01",
                "endDate": "2023-04-05"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBodyR1)
                .when()
                .post("/bookings")
                .then()
                .statusCode(200);

        String requestBodyR2 = """
            {
                "employeeId": "456",
                "roomId": "101",
                "startDate": "2023-03-31",
                "endDate": "2023-04-03"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBodyR2)
                .when()
                .post("/bookings")
                .then()
                .statusCode(409); // Verificar que se devuelve un código de estado HTTP 409 Conflict
    }
}
