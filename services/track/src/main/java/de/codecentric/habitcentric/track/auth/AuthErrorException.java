package de.codecentric.habitcentric.track.auth;

import de.codecentric.habitcentric.track.error.ApiErrorException;

public class AuthErrorException extends ApiErrorException {

  public AuthErrorException(AuthError error, Throwable cause, Object... templateProperties) {
    super(error, cause, templateProperties);
  }

  public AuthErrorException(AuthError error, Object... templateProperties) {
    super(error, templateProperties);
  }
}
