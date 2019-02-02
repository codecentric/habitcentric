package de.codecentric.hc.track.monitoring;

import de.codecentric.hc.track.RestAssuredTest;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MonitoringRestAssuredTest extends RestAssuredTest {

    @Test
    public void healthEndpointShouldReturnHttpStatusCode200() {
        given()
                .when().get("/actuator/health")
                .then().statusCode(200).body("status", equalTo("UP"));
    }
}
