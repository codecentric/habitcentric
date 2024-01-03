package de.codecentric.habitcentric.track.habit.matcher;

import static de.codecentric.habitcentric.track.habit.HabitTrackingError.CONSTRAINT_VIOLATION;
import static de.codecentric.habitcentric.track.utils.ApiErrorMatcher.hasError;

import org.hamcrest.Matcher;

public class HabitApiMatcher {

  public static Matcher<String> hasUserIdViolationError() {
    return hasError(CONSTRAINT_VIOLATION, "must not be blank and size must be between 5 and 64");
  }

  public static Matcher<String> hasHabitIdViolationError() {
    return hasError(CONSTRAINT_VIOLATION, "must be a valid UUID");
  }
}
