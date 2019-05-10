package de.codecentric.hc.gateway.routing;

import static de.codecentric.hc.gateway.security.ApplicationUser.DEFAULT;

import de.codecentric.hc.gateway.testing.WebTest;
import org.junit.Test;

public class UiTest extends WebTest {

  @Test
  public void getUiShouldReturnTheHabitUi() {
    get("/ui/", DEFAULT)
        .expectStatus()
        .isOk()
        .expectBody()
        .xpath("/html/head/title/text()")
        .isEqualTo("hc-ui");
  }
}
