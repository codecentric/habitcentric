package de.codecentric.hc.gateway.routing;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;
import static de.codecentric.hc.gateway.security.ApplicationUser.Username.DEFAULT;
import static org.hamcrest.Matchers.is;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class ReportIntTest extends WebTest {

  @Test
  @WithMockUser(username = DEFAULT, roles = USER)
  public void getAchievementReportShouldReturnAchievementReport() {
    get("/report/achievement")
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.week")
        .value(is(0.5))
        .jsonPath("$.month")
        .value(is(0.75));
  }
}
