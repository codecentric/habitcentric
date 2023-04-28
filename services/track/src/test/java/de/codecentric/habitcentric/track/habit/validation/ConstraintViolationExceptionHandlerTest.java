package de.codecentric.habitcentric.track.habit.validation;

import static de.codecentric.habitcentric.track.habit.HabitTrackingError.CONSTRAINT_VIOLATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.codecentric.habitcentric.track.error.ApiErrorResponse;
import de.codecentric.habitcentric.track.error.ApiErrorResponseAssertion;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class ConstraintViolationExceptionHandlerTest implements ApiErrorResponseAssertion {

  private ConstraintViolationExceptionHandler handler = new ConstraintViolationExceptionHandler();

  @Test
  public void canHandleConstraintViolationExceptions() {
    String message = "ABC";
    ApiErrorResponse response =
        handler.handle(new ConstraintViolationException(createViolations(message)));
    assertThat(response.getErrors()).hasSize(1);
    assertEquals(CONSTRAINT_VIOLATION, response.getErrors().get(0), message);
  }

  private Set<ConstraintViolation<?>> createViolations(String... messages) {
    return Stream.of(messages)
        .map(
            message -> {
              ConstraintViolation<?> violation = mock(ConstraintViolation.class);
              when(violation.getMessage()).thenReturn(message);
              return violation;
            })
        .collect(Collectors.toSet());
  }
}
