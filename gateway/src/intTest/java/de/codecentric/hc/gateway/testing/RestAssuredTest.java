package de.codecentric.hc.gateway.testing;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

public abstract class RestAssuredTest extends GatewayTest {

  @BeforeEach
  public void configureRestAssured() {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }
}
