package de.codecentric.hc.gateway.security;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.MONITORING;
import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class SecurityConfigTest extends WebTest {

  @Test
  public void requestsWithoutAuthShouldBeUnauthorized() {
    get("/actuator").expectStatus().isUnauthorized();
    get("/habits").expectStatus().isUnauthorized();
    post("/habits", "{}").expectStatus().isUnauthorized();
    delete("/habits/123").expectStatus().isUnauthorized();
    get("/track").expectStatus().isUnauthorized();
    put("/track", "{}").expectStatus().isUnauthorized();
    get("/ui/").expectStatus().isUnauthorized();
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
