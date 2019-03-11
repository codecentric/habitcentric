package de.codecentric.hc.habit;

import de.codecentric.hc.habit.testing.RestAssuredTest;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MonitoringTest extends RestAssuredTest {

    @Test
    public void healthCheck() {
        given()
                .when().get("/actuator/health")
                .then().statusCode(200).body("status", equalTo("UP"));
    }
}
