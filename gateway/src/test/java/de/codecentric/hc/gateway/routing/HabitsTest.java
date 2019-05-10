package de.codecentric.hc.gateway.routing;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.contains;

import de.codecentric.hc.gateway.security.ApplicationUser;
import de.codecentric.hc.gateway.testing.RestAssuredTest;
import org.junit.Test;

public class HabitsTest extends RestAssuredTest {

  @Test
  public void getHabitsShouldReturnHabits() {
    ApplicationUser user = ApplicationUser.DEFAULT;
    given()
        .auth()
        .basic(user.getUsername(), user.getPassword())
        .when()
        .get("/habits")
        .then()
        .statusCode(200)
        .body("name", contains("Jogging", "Meditate", "Play guitar"));
  }

  @Test
  public void postHabitsShouldReturnCreated() {
    ApplicationUser user = ApplicationUser.DEFAULT;
    given()
        .auth()
        .basic(user.getUsername(), user.getPassword())
        .contentType(JSON)
        .body("{\"name\": \"Jogging\"}")
        .when()
        .post("/habits")
        .then()
        .statusCode(201);
  }

  @Test
  public void deleteHabitsShouldReturnOk() {
    ApplicationUser user = ApplicationUser.DEFAULT;
    given()
        .auth()
        .basic(user.getUsername(), user.getPassword())
        .when()
        .delete("/habits/123")
        .then()
        .statusCode(200);
  }
}
