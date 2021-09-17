package de.codecentric.habitcentric.track.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.habitcentric.track.error.dummy.DummyError;
import de.codecentric.habitcentric.track.error.dummy.DummyErrorException;
import org.junit.jupiter.api.Test;

public class ApiErrorExceptionTest {

  @Test
  public void withError() {
    DummyError error = DummyError.DEFAULT;
    ApiErrorException exception = new DummyErrorException(error);
    assertThat(exception).hasMessage(expectedMessage(error));
    assertThat(exception.getTemplateProperties()).isEmpty();
  }

  @Test
  public void withErrorAndCause() {
    DummyError error = DummyError.DEFAULT;
    Exception cause = new Exception("DEF");
    ApiErrorException exception = new DummyErrorException(error, cause);
    assertThat(exception).hasMessage(expectedMessage(error)).hasCause(cause);
  }

  @Test
  public void withErrorAndTemplateProperties() {
    DummyError error = DummyError.DEFAULT_WITH_PLACEHOLDER;
    Object[] templateProperties = {"ABC"};
    ApiErrorException exception = new DummyErrorException(error, templateProperties);
    assertThat(exception).hasMessage(expectedMessage(error, templateProperties));
  }

  @Test
  public void withErrorAndCauseAndTemplateProperties() {
    DummyError error = DummyError.DEFAULT_WITH_PLACEHOLDER;
    Object[] templateProperties = {"ABC"};
    Exception cause = new Exception("DEF");
    ApiErrorException exception = new DummyErrorException(error, cause, templateProperties);
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
