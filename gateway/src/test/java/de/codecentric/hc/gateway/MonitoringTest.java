package de.codecentric.hc.gateway;

import de.codecentric.hc.gateway.testing.RestAssuredTest;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MonitoringTest extends RestAssuredTest {

    @Test
    public void getHealthShouldReturnOk() {
        given()
                .when().get("/actuator/health")
                .then().statusCode(200).body("status", equalTo("UP"));
    }
}
