package de.codecentric.hc.habit.api.doc;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.HTML;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import de.codecentric.hc.habit.testing.RestAssuredTest;
import org.junit.jupiter.api.Test;

public class ApiDocIntTest extends RestAssuredTest {

  @Test
  public void shouldReturnOpenApiSpecAsJson() {
    given()
        .when()
        .get("/v2/api-docs")
        .then()
        .statusCode(200)
        .contentType(JSON)
        .body("info.title", equalTo("Habit API"))
        .body("info.description", equalTo("Service to manage habits."));
  }

  @Test
  public void shouldDisplaySwaggerUi() {
    given()
        .when()
        .get("/swagger-ui.html")
        .then()
        .statusCode(200)
        .contentType(HTML)
        .body(containsString("<title>Swagger UI</title>"));
  }
}
