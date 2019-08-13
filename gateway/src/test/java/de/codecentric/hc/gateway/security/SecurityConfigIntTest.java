package de.codecentric.hc.gateway.security;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.MONITORING;
import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class SecurityConfigIntTest extends WebTest {

  @Test
  public void requestsWithoutAuthShouldBeUnauthorized() {
    String expectedLocation = "/oauth2/authorization/keycloak";
    get("/actuator")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    get("/habits")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    post("/habits", "{}")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    delete("/habits/123")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    get("/track")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    put("/track", "{}")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    get("/ui/").expectStatus().isFound().expectHeader().valueMatches("Location", expectedLocation);
  }

  @Test
  public void requestsWithoutAuthShouldBeOk() {
    get("/favicon.ico").expectStatus().isOk();
    get("/actuator/health").expectStatus().isOk();
  }

  @Test
  @WithMockUser(roles = USER)
  public void actuatorRequestsWithInvalidUserRoleShouldBeForbidden() {
    get("/actuator").expectStatus().isForbidden();
  }

  @Test
  @WithMockUser(roles = MONITORING)
  public void requestsWithInvalidUserRoleShouldBeForbidden() {
    get("/habits").expectStatus().isForbidden();
    post("/habits", "{}").expectStatus().isForbidden();
    delete("/habits/123").expectStatus().isForbidden();
    get("/track").expectStatus().isForbidden();
    put("/track", "{}").expectStatus().isForbidden();
    get("/ui/").expectStatus().isForbidden();
  }
}
