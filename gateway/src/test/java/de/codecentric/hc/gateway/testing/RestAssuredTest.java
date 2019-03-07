package de.codecentric.hc.gateway.testing;

import io.restassured.RestAssured;
import org.junit.Before;

public abstract class RestAssuredTest extends GatewayTest {

    @Before
    public void configureRestAssured() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
