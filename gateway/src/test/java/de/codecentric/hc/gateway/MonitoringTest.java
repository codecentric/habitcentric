package de.codecentric.hc.gateway;

import de.codecentric.hc.gateway.security.ApplicationUser;
import de.codecentric.hc.gateway.testing.RestAssuredTest;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class MonitoringTest extends RestAssuredTest {

    @Test
    public void getActuatorShouldReturnOk() {
        ApplicationUser user = ApplicationUser.MONITORING;
        given().auth().basic(user.getName(), user.getPassword())
                .when().get("/actuator")
                .then().statusCode(200).body("_links.self.href", containsString("http://localhost"));
    }
}
