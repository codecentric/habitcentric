package de.codecentric.habitcentric.track.error;

import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ApiErrorExceptionHandler {

  @ExceptionHandler(ApiErrorException.class)
  @ResponseBody
  protected ResponseEntity<ApiErrorResponse> handle(ApiErrorException ex) {
    return toResponse(ex.getError(), ex.getTemplateProperties());
  }

  protected ResponseEntity<ApiErrorResponse> toResponse(
      ApiError error, Object... templateProperties) {
    return new ResponseEntity<>(
        new ApiErrorResponse(Arrays.asList(toResponseError(error, templateProperties))),
        HttpStatus.valueOf(error.getStatusCode()));
  }

  protected ApiErrorResponse.Error toResponseError(ApiError error, Object... templateProperties) {
    return ApiErrorResponse.Error.builder()
        .code(error.getCode())
        .title(error.getTitle())
        .detail(error.getDetail(templateProperties).orElse(null))
        .build();
  }
}
