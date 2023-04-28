package de.codecentric.habitcentric.track.habit.validation;

import static de.codecentric.habitcentric.track.habit.HabitTrackingError.CONSTRAINT_VIOLATION;

import de.codecentric.habitcentric.track.error.ApiErrorExceptionHandler;
import de.codecentric.habitcentric.track.error.ApiErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ConstraintViolationExceptionHandler extends ApiErrorExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  ApiErrorResponse handle(ConstraintViolationException ex) {
    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
    return new ApiErrorResponse(
        violations.stream().map(this::toResponseError).collect(Collectors.toList()));
  }

  private ApiErrorResponse.Error toResponseError(ConstraintViolation<?> violation) {
    return toResponseError(CONSTRAINT_VIOLATION, violation.getMessage());
  }
}
