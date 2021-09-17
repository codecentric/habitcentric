package de.codecentric.hc.gateway.filters;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserIdMissingExceptionTest {

  private UserIdMissingException exception;

  @BeforeEach
  public void beforeEach() {
    exception = new UserIdMissingException();
  }

  @Test
  public void shouldHaveExpectedMessage() {
    assertThat(exception.getMessage())
        .isEqualTo("401 UNAUTHORIZED \"The required user ID is missing.\"");
  }
}
