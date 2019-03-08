package de.codecentric.hc.gateway.security;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.Test;

import static de.codecentric.hc.gateway.security.ApplicationUser.DEFAULT;
import static de.codecentric.hc.gateway.security.ApplicationUser.MONITORING;

public class SecurityConfigTest extends WebTest {

    @Test
    public void requestsWithoutAuthShouldBeUnauthorized() {
        get("/actuator/health").expectStatus().isUnauthorized();
        get("/habits").expectStatus().isUnauthorized();
        post("/habits", "{}").expectStatus().isUnauthorized();
        delete("/habits/123").expectStatus().isUnauthorized();
        get("/track").expectStatus().isUnauthorized();
        put("/track", "{}").expectStatus().isUnauthorized();
        get("/ui/").expectStatus().isUnauthorized();
    }

    @Test
    public void requestsWithInvalidUserRoleShouldBeForbidden() {
        get("/actuator/health", DEFAULT).expectStatus().isForbidden();
        get("/habits", MONITORING).expectStatus().isForbidden();
        post("/habits", MONITORING, "{}").expectStatus().isForbidden();
        delete("/habits/123", MONITORING).expectStatus().isForbidden();
        get("/track", MONITORING).expectStatus().isForbidden();
        put("/track", MONITORING, "{}").expectStatus().isForbidden();
        get("/ui/", MONITORING).expectStatus().isForbidden();
    }
}
