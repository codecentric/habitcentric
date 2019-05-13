package de.codecentric.habitcentric.track.error;

import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.habitcentric.track.error.ApiErrorResponse.Error;
import org.junit.Test;

public class ApiErrorResponseMatcherTest {

  @Test
  public void describeToShouldReturnExpectedErrorsAsJson() {

    final Error[] errors = {
      null,
      Error.builder().build(),
      Error.builder().code("ABC").build(),
      Error.builder().code("DEF").title("GHI").build(),
      Error.builder().code("JKL").title("MNO").detail("PQR").build()
    };

    final String expected =
        "{\"errors\":[{},{\"code\":\"ABC\"},{\"code\":\"DEF\",\"title\":\"GHI\"},{\"code\":\"JKL\",\"title\":\"MNO\",\"detail\":\"PQR\"}]}";

    assertThat(ApiErrorResponseMatcher.hasApiErrors(errors).toString()).isEqualTo(expected);
  }
}
