package de.codecentric.hc.gateway.security;

import static de.codecentric.hc.gateway.security.ApplicationUser.lptUsers;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ApplicationUserTest {

  @Test
  public void shouldHave100UsersForLoadAndPerformanceTesting() {
    assertThat(lptUsers).size().isEqualTo(100);
    assertThat(lptUsers).contains("user000001", "user000100");
  }
}
