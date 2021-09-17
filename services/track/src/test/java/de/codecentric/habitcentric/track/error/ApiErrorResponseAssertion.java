package de.codecentric.habitcentric.track.error;

import static org.assertj.core.api.Assertions.assertThat;

public interface ApiErrorResponseAssertion {

  default void assertEquals(
      ApiError expected, ApiErrorResponse.Error actual, Object... templateProperties) {
    assertThat(expected.getCode()).isEqualTo(actual.getCode());
    assertThat(expected.getTitle()).isEqualTo(actual.getTitle());
    if (expected.getDetailTemplate().isPresent()) {
      assertThat(expected.getDetail(templateProperties).get()).isEqualTo(actual.getDetail());
    }
    assertThat(actual.getId()).isNotBlank();
  }
}
