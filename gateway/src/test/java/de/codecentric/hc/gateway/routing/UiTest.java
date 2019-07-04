package de.codecentric.hc.gateway.routing;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;
import static de.codecentric.hc.gateway.security.ApplicationUser.Username.DEFAULT;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class UiTest extends WebTest {

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
}
