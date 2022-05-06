package de.codecentric.hc.gateway.routing;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;
import static de.codecentric.hc.gateway.security.ApplicationUser.Username.DEFAULT;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class UiIntTest extends WebTest {

  @Test
  @WithMockUser(username = DEFAULT, roles = USER)
  public void getUiShouldReturnTheHabitUi() {
    get("/ui/")
        .expectStatus()
        .isOk()
        .expectBody()
        .xpath("/html/head/title/text()")
        .isEqualTo("hc-ui");
  }

  @Test
  @WithMockUser(username = DEFAULT, roles = USER)
  public void getUiWithAnyRouteShouldReturnTheHabitUi() {
    get("/ui/any")
        .expectStatus()
        .isOk()
        .expectBody()
        .xpath("/html/head/title/text()")
        .isEqualTo("hc-ui");
  }

  @Test
  @WithMockUser(username = DEFAULT, roles = USER)
  public void getUiWithOverviewRouteShouldReturnTheHabitUiOverview() {
    get("/ui/overview")
        .expectStatus()
        .isOk()
        .expectBody()
        .xpath("/html/head/title/text()")
        .isEqualTo("hc-ui overview");
  }
}
