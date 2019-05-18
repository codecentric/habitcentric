package de.codecentric.habitcentric.track.error;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  ApiErrorResponse handleConstraintViolations(ConstraintViolationException ex) {
    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
    return new ApiErrorResponse(
        violations.stream().map(this::toApiError).collect(Collectors.toList()));
  }

  private ApiError toApiError(ConstraintViolation<?> violation) {
    String annotationName =
        violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
    return ApiError.builder()
        .code(String.format("ConstraintViolation.%s", annotationName))
        .detail(violation.getMessage())
        .build();
  }
}
