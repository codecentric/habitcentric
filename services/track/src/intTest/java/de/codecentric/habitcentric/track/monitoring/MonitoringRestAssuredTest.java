package de.codecentric.habitcentric.track.monitoring;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import de.codecentric.habitcentric.track.RestAssuredTest;
import org.junit.jupiter.api.Test;

public class MonitoringRestAssuredTest extends RestAssuredTest {

  @Test
  public void healthEndpointShouldReturnHttpStatusCode200() {
    given().when().get("/actuator/health").then().statusCode(200).body("status", equalTo("UP"));
  }
}
