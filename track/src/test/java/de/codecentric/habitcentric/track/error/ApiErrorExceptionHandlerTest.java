package de.codecentric.habitcentric.track.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import de.codecentric.habitcentric.track.error.dummy.DummyError;
import de.codecentric.habitcentric.track.error.dummy.DummyErrorException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class ApiErrorExceptionHandlerTest implements ApiErrorResponseAssertion {

  private ApiErrorExceptionHandler handler;

  @Before
  public void beforeEach() {
    handler = new ApiErrorExceptionHandler();
  }

  @Test
  public void toResponseErrorShouldMapApiErrors() {
    ApiError apiError =
        DummyError.builder().code("123").title("ABC").detailTemplate(Optional.of("DEF")).build();
    ApiErrorResponse.Error responseError = handler.toResponseError(apiError);
    assertThat(responseError.getCode()).isEqualTo("123");
    assertThat(responseError.getTitle()).isEqualTo("ABC");
    assertThat(responseError.getDetail()).isEqualTo("DEF");
  }

  @Test
  public void toResponseErrorShouldMapApiErrorsWithoutDetail() {
    ApiError apiError = DummyError.builder().code("123").title("ABC").build();
    ApiErrorResponse.Error responseError = handler.toResponseError(apiError);
    assertThat(responseError.getCode()).isEqualTo("123");
    assertThat(responseError.getTitle()).isEqualTo("ABC");
    assertThat(responseError.getDetail()).isNull();
  }

  @Test
  public void toResponseErrorShouldMapApiErrorsWithPlaceholders() {
    ApiError apiError =
        DummyError.builder()
            .code("123")
            .title("ABC")
            .detailTemplate(Optional.of("A{0}C{1}E"))
            .build();
    ApiErrorResponse.Error responseError = handler.toResponseError(apiError, "B", "D");
    assertThat(responseError.getCode()).isEqualTo("123");
    assertThat(responseError.getTitle()).isEqualTo("ABC");
    assertThat(responseError.getDetail()).isEqualTo("ABCDE");
  }

  @Test
  public void handleShouldMapApiExceptions() {
    DummyError error =
        DummyError.builder().code("123").title("ABC").statusCode(I_AM_A_TEAPOT.value()).build();
    ApiErrorException exception = new DummyErrorException(error);
    ResponseEntity<ApiErrorResponse> response = handler.handle(exception);
    assertThat(response.getStatusCodeValue()).isEqualTo(error.getStatusCode());
    assertThat(response.getBody().getErrors()).hasSize(1);
    assertEquals(error, response.getBody().getErrors().get(0));
  }

  @Test
  public void handleShouldMapApiExceptionsWithCauseAndTemplateProperties() {
    DummyError error =
        DummyError.builder()
            .code("123")
            .title("ABC")
            .detailTemplate(Optional.of("{0}"))
            .statusCode(NOT_FOUND.value())
            .build();
    Object[] templateProperties = {"ABC"};
    Exception cause = new IllegalArgumentException("CAUSE");
    ApiErrorException exception = new DummyErrorException(error, cause, templateProperties);
    ResponseEntity<ApiErrorResponse> response = handler.handle(exception);
    assertThat(response.getStatusCodeValue()).isEqualTo(error.getStatusCode());
    assertThat(response.getBody().getErrors()).hasSize(1);
    assertEquals(error, response.getBody().getErrors().get(0), templateProperties);
  }
}
