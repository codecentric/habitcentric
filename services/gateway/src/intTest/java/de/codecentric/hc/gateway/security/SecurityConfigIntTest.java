package de.codecentric.hc.gateway.security;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.MONITORING;
import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.security.test.context.support.WithMockUser;

@AutoConfigureWebTestClient
public class SecurityConfigIntTest extends WebTest {

  @Test
  public void requestsWithoutAuthShouldBeOk() {
    get("/favicon.ico").expectStatus().isOk();
    get("/actuator/health").expectStatus().isOk();
    get("/ui/").expectStatus().isOk();
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
    get("/report/achievement").expectStatus().isForbidden();
    get("/ui/overview").expectStatus().isForbidden();
  }
}
