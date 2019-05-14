package de.codecentric.habitcentric.track.habit;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.habitcentric.track.error.ApiError;
import org.junit.Test;

public class HabitTrackingErrorExceptionTest {

  @Test
  public void withErrorAndTemplateProperties() {
    HabitTrackingError error = HabitTrackingError.CONSTRAINT_VIOLATION;
    Object[] templateProperties = {"ABC"};
    HabitTrackingErrorException exception =
        new HabitTrackingErrorException(error, templateProperties);
    assertThat(exception).hasMessage(expectedMessage(error, templateProperties));
  }

  @Test
  public void withErrorAndCauseAndTemplateProperties() {
    HabitTrackingError error = HabitTrackingError.CONSTRAINT_VIOLATION;
    Object[] templateProperties = {"ABC"};
    Exception cause = new Exception("DEF");
    HabitTrackingErrorException exception =
        new HabitTrackingErrorException(error, cause, templateProperties);
    assertThat(exception).hasMessage(expectedMessage(error, templateProperties));
  }

  private String expectedMessage(ApiError error, Object... templateProperties) {
    return error.getDetailTemplate().isPresent()
        ? format(
            "%s - %s [%s]",
            error.getTitle(), error.getDetail(templateProperties).get(), error.getCode())
        : format("%s [%s]", error.getTitle(), error.getCode());
  }
}
