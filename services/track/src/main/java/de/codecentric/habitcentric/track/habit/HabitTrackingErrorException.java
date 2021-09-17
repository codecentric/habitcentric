package de.codecentric.habitcentric.track.habit;

import de.codecentric.habitcentric.track.error.ApiErrorException;

public class HabitTrackingErrorException extends ApiErrorException {

  public HabitTrackingErrorException(
      HabitTrackingError error, Throwable cause, Object... templateProperties) {
    super(error, cause, templateProperties);
  }

  public HabitTrackingErrorException(HabitTrackingError error, Object... templateProperties) {
    super(error, templateProperties);
  }
}
