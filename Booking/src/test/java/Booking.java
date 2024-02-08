import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class Booking {

    @Test
    public void PostTestAuth() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com/auth";
        String requestBody = "{ \"username\": \"admin\", \"password\": \"password123\" }";
        String body = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/token")
                .then()
                .extract()
                .body().asString();

        System.out.println(body);
    }

    // Obtener todos los id de las Reservas
    @Test
    public static void GetAllBookingIds() {
        String[] args;

       RestAssured.baseURI = "https://restful-booker.herokuapp.com";
       Response response = RestAssured.get("/booking");
       System.out.println("Código de estado de la respuesta: " + response.getStatusCode());
        System.out.println("Cuerpo de la respuesta: " + response.getBody().asString());
    }

    @Test // Obtener una reserva por el Id
    public void GetBookingById() {
        String[] args;
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        int bookingId =236;
        Response response = RestAssured.given().get("/booking/" + bookingId);
        System.out.println("Código de estado de la respuesta: " + response.getStatusCode());
        System.out.println("Cuerpo de la respuesta: " + response.getBody().asString());
    }

    @Test //Crear reserva
    public void createBooking() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("firstname", "John");
        bookingDetails.put("lastname", "Doe");
        bookingDetails.put("totalprice", 200);
        bookingDetails.put("depositpaid", true);

        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2024-02-10");
        bookingDates.put("checkout", "2024-02-15");

        bookingDetails.put("bookingdates", bookingDates);
        bookingDetails.put("additionalneeds", "Breakfast");

        try {
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(bookingDetails)
                    .post("/booking");

            System.out.println("Código de estado de la respuesta: " + response.getStatusCode());

            if (response.getStatusCode() == 200) {
                System.out.println("La reserva se creó correctamente.");
                System.out.println("Cuerpo de la respuesta: " + response.getBody().asString());
            } else {
                System.out.println("Error al crear la reserva. Código de estado: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error al procesar la solicitud: " + e.getMessage());
        }
    }

    @Test // Actualizar reserva
    public void updateBookingWithToken() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        String token = given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"admin\", \"password\": \"password123\" }")
                .when()
                .post("/auth")
                .then()
                .extract()
                .path("token");

        int bookingId = 2753;
        String requestBody = "{ \"firstname\": \"UpdatedFirstName\", \"lastname\": \"UpdatedLastName\" }";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "token " + token)
                .pathParam("bookingId", bookingId)
                .body(requestBody)
                .when()
                .patch("/booking/{bookingId}")
                .then()
                .log().all();
        //.statusCode(200);
    }

    @Test //Actualización Parcial de la reserva
    public void patchBookingWithToken() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String token = given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"admin\", \"password\": \"password123\" }")
                .when()
                .post("/auth")
                .then()
                .extract()
                .path("token");

        int bookingId = 675;


        String requestBody = "{ \"firstname\": \"Dana\", \"lastname\": \"Luna\" }";

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "token " + token)
                .pathParam("bookingId", bookingId)
                .body(requestBody)
                .when()
                .patch("/booking/{bookingId}")
                .then()
                .log().all();

    }

    @Test // Eliminar una reserva
    public void deleteBookingWithToken() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String token = given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"admin\", \"password\": \"password123\" }")
                .when()
                .post("/auth")
                .then()
                .extract()
                .path("token");
        System.out.println(token);
        int bookingId = 207;
        given()
                .header("Authorization", "token " + token)
                .pathParam("bookingId", bookingId)
                .when()
                .delete("/booking/{bookingId}")
                .then()
                .log().all()
                .statusCode(201);
    }

    @Test
    public void pingEndpoint() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        given()
                .when()
                .get("/ping")
                .then()
                .log().all()
                .statusCode(201);
    }
}
