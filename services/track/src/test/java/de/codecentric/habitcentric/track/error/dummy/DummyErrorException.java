package de.codecentric.habitcentric.track.error.dummy;

import de.codecentric.habitcentric.track.error.ApiErrorException;

public class DummyErrorException extends ApiErrorException {

  public DummyErrorException(DummyError error, Throwable cause, Object... templateProperties) {
    super(error, cause, templateProperties);
  }

  public DummyErrorException(DummyError error, Object... templateProperties) {
    super(error, templateProperties);
  }
}
