package de.codecentric.hc.habit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import de.codecentric.hc.habit.testing.RestAssuredTest;
import org.junit.Test;

public class MonitoringTest extends RestAssuredTest {

  @Test
  public void healthCheck() {
    given().when().get("/actuator/health").then().statusCode(200).body("status", equalTo("UP"));
  }
}
