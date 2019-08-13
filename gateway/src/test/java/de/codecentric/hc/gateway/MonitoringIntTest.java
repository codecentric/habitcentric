package de.codecentric.hc.gateway;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.MONITORING;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

import de.codecentric.hc.gateway.testing.RestAssuredTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class MonitoringIntTest extends RestAssuredTest {

  @Test
  @WithMockUser(roles = MONITORING)
  public void getActuatorShouldReturnOk() {
    when()
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
