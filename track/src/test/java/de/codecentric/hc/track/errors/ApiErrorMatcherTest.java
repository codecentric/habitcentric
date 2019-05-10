package de.codecentric.hc.track.errors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ApiErrorMatcherTest {

  @Test
  public void describeToShouldReturnExpectedErrorsAsJson() {

    final ApiError[] errors = {
      null,
      ApiError.builder().build(),
      ApiError.builder().code("ABC").build(),
      ApiError.builder().code("DEF").title("GHI").build(),
      ApiError.builder().code("JKL").title("MNO").detail("PQR").build()
    };

    final String expected =
        "{\"errors\":[{},{\"code\":\"ABC\"},{\"code\":\"DEF\",\"title\":\"GHI\"},{\"code\":\"JKL\",\"title\":\"MNO\",\"detail\":\"PQR\"}]}";

    assertThat(ApiErrorMatcher.hasApiErrors(errors).toString()).isEqualTo(expected);
  }
}
