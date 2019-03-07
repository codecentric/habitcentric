package de.codecentric.hc.gateway.security;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.Test;

import static de.codecentric.hc.gateway.security.ApplicationUser.DEFAULT;

public class SecurityConfigTest extends WebTest {

    @Test
    public void requestsWithoutAuthShouldBeUnauthorized() {
        get("/actuator/health").expectStatus().isUnauthorized();
    }

    @Test
    public void requestsWithInvalidUserRoleShouldBeForbidden() {
        get("/actuator/health", DEFAULT).expectStatus().isForbidden();
    }
}
