package de.codecentric.hc.gateway;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

import de.codecentric.hc.gateway.security.ApplicationUser;
import de.codecentric.hc.gateway.testing.RestAssuredTest;
import org.junit.Test;

public class MonitoringTest extends RestAssuredTest {

  @Test
  public void getActuatorShouldReturnOk() {
    ApplicationUser user = ApplicationUser.MONITORING;
    given()
        .auth()
        .basic(user.getUsername(), user.getPassword())
        .when()
        .get("/actuator")
        .then()
        .statusCode(200)
        .body("_links.self.href", containsString("http://localhost"));
  }

  @Test
  public void getActuatorHealthShouldReturnOk() {
    when().get("/actuator/health").then().statusCode(200).body("status", containsString("UP"));
  }
}
