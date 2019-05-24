package de.codecentric.habitcentric.track.auth;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.habitcentric.track.error.ApiError;
import org.junit.Test;

public class AuthErrorExceptionTest {

  @Test
  public void withError() {
    AuthError error = AuthError.JWT_TOKEN_MISSING;
    AuthErrorException exception = new AuthErrorException(error);
    assertThat(exception).hasMessage(expectedMessage(error));
    assertThat(exception.getTemplateProperties()).isEmpty();
  }

  @Test
  public void withErrorAndCause() {
    AuthError error = AuthError.JWT_TOKEN_MISSING;
    Exception cause = new Exception("DEF");
    AuthErrorException exception = new AuthErrorException(error, cause);
    assertThat(exception).hasMessage(expectedMessage(error)).hasCause(cause);
  }

  @Test
  public void withErrorAndTemplateProperties() {
    AuthError error = AuthError.JWT_TOKEN_DECODING_ERROR;
    Object[] templateProperties = {"ABC"};
    AuthErrorException exception = new AuthErrorException(error, templateProperties);
    assertThat(exception).hasMessage(expectedMessage(error, templateProperties));
  }

  @Test
  public void withErrorAndCauseAndTemplateProperties() {
    AuthError error = AuthError.JWT_TOKEN_DECODING_ERROR;
    Object[] templateProperties = {"ABC"};
    Exception cause = new Exception("DEF");
    AuthErrorException exception = new AuthErrorException(error, cause, templateProperties);
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
