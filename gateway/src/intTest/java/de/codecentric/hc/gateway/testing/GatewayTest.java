package de.codecentric.hc.gateway.testing;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("intTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
      "gateway.target.uri.habit=http://localhost:${wiremock.server.port}",
      "gateway.target.uri.track=http://localhost:${wiremock.server.port}",
      "gateway.target.uri.ui=http://localhost:${wiremock.server.port}"
    })
@AutoConfigureWireMock(
    port = 0,
    stubs = "file:src/intTest/resources/mappings",
    files = "file:src/intTest/resources")
public abstract class GatewayTest {

  @LocalServerPort protected int port;
}
