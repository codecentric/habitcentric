package de.codecentric.habitcentric.track.habit.matcher;

import static de.codecentric.habitcentric.track.habit.matcher.HabitApiMatcher.hasHabitIdViolationError;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.hamcrest.Description;
import org.junit.jupiter.api.Test;

public class HabitApiMatcherTest {

  private final String habitIdViolationErrorResponse =
      "{\"errors\":[{\"code\":\"TRACK_101\",\"title\":\"Constraint violation\",\"detail\":\"must be a valid UUID\",\"id\":\"123\"}]}";
  private final String mismatchErrorResponse =
      "{\"errors\":[{\"code\":\"TRACK_101\",\"title\":\"Constraint violation\",\"detail\":\"MISMATCH\",\"id\":\"123\"}]}";
  private final String multipleErrorsResponse =
      "{\"errors\":[{},{\"code\":\"ABC\"},{\"code\":\"DEF\",\"title\":\"GHI\"},{\"code\":\"JKL\",\"title\":\"MNO\",\"detail\":\"PQR\"}]}";

  @Test
  public void hasErrorShouldMatchResponsesWithRelevantError() {
    assertThat(habitIdViolationErrorResponse, hasHabitIdViolationError());
  }

  @Test
  public void hasErrorShouldNotMatchResponsesWithoutRelevantError() {
    assertThat("{\"errors\":[]}", not(hasHabitIdViolationError()));
    assertThat(mismatchErrorResponse, not(hasHabitIdViolationError()));
    assertThat(multipleErrorsResponse, not(hasHabitIdViolationError()));
  }

  @Test
  public void hasErrorShouldThrowExceptionWhenResponsesAreInvalid() {
    assertThrows(IllegalArgumentException.class, () -> assertThat("", hasHabitIdViolationError()));
  }

  @Test
  public void describeToShouldReturnExpectedErrorsAsJson() {
    Description description = mock(Description.class);
    hasHabitIdViolationError().describeTo(description);
    verify(description)
        .appendText(
            "{\"code\":\"TRACK_101\",\"title\":\"Constraint violation\",\"detail\":\"must be a valid UUID\"}");
  }
}
