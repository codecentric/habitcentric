package de.codecentric.hc.gateway.routing;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;
import static de.codecentric.hc.gateway.security.ApplicationUser.Username.DEFAULT;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.contains;

import de.codecentric.hc.gateway.testing.RestAssuredTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class HabitsIntTest extends RestAssuredTest {

  @Test
  @WithMockUser(username = DEFAULT, roles = USER)
  public void getHabitsShouldReturnHabits() {
    when()
        .get("/habits")
        .then()
        .statusCode(200)
        .body("name", contains("Jogging", "Meditate", "Play guitar"));
  }

  @Test
  @WithMockUser(username = DEFAULT, roles = USER)
  public void postHabitsShouldReturnCreated() {
    given()
        .contentType(JSON)
        .body("{\"name\": \"Jogging\"}")
        .when()
        .post("/habits")
        .then()
        .statusCode(201);
  }

  @Test
  @WithMockUser(username = DEFAULT, roles = USER)
  public void deleteHabitsShouldReturnOk() {
    when().delete("/habits/123").then().statusCode(200);
  }
}
