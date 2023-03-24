package de.codecentric.habitcentric.track.auth;

import static de.codecentric.habitcentric.track.auth.AuthError.JWT_TOKEN_MISSING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.codecentric.habitcentric.track.error.ApiError;
import de.codecentric.habitcentric.track.error.ApiErrorException;
import de.codecentric.habitcentric.track.habit.validation.UserId;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserIdArgumentResolverTest {

  private UserIdArgumentResolver resolver;

  @BeforeEach
  public void beforeEach() {
    resolver = new UserIdArgumentResolver();
    resolver.setJwtDecoder(new InsecureJwtDecoder());
  }

  @Test
  public void shouldSupportParametersWithUserIdAnnotation() {
    MethodParameter parameter = mock(MethodParameter.class);
    when(parameter.getParameterAnnotation(UserId.class)).thenReturn(mock(UserId.class));
    assertThat(resolver.supportsParameter(parameter)).isTrue();
  }

  @Test
  public void shouldNotSupportParametersWithoutUserIdAnnotation() {
    MethodParameter parameter = mock(MethodParameter.class);
    assertThat(resolver.supportsParameter(parameter)).isFalse();
  }

  @Test
  public void shouldExtractUserIdFromJwtToken() {
    String authorization =
        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmMuZGVmIn0"
            + ".WwSUwDaAy06itFh4asthobE0SNJihIsxaw6koNNsjZI";
    assertThat(
            resolver.resolveArgument(
                mock(MethodParameter.class),
                mock(ModelAndViewContainer.class),
                requestWithAuthorization(authorization),
                mock(WebDataBinderFactory.class)))
        .isEqualTo("abc.def");
  }

  @Test
  public void shouldThrowAnExceptionWhenTheAuthorizationHeaderIsMissing() {
    String authorization = null;
    assertThatExceptionOfType(ApiErrorException.class)
        .isThrownBy(
            () ->
                resolver.resolveArgument(
                    mock(MethodParameter.class),
                    mock(ModelAndViewContainer.class),
                    requestWithAuthorization(null),
                    mock(WebDataBinderFactory.class)))
        .has(expectedError(JWT_TOKEN_MISSING));
  }

  @Test
  public void shouldThrowAnExceptionWhenTheAuthorizationHeaderIsBasicAuth() {
    String authorization = "Basic dXNlcjoxMjM=";
    assertThatExceptionOfType(ApiErrorException.class)
        .isThrownBy(
            () ->
                resolver.resolveArgument(
                    mock(MethodParameter.class),
                    mock(ModelAndViewContainer.class),
                    requestWithAuthorization(authorization),
                    mock(WebDataBinderFactory.class)))
        .has(expectedError(JWT_TOKEN_MISSING));
  }

  private NativeWebRequest requestWithAuthorization(String headerValue) {
    HttpServletRequest httpRequest = mock(HttpServletRequest.class);
    when(httpRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(headerValue);
    NativeWebRequest webRequest = mock(NativeWebRequest.class);
    when(webRequest.getNativeRequest()).thenReturn(httpRequest);
    return webRequest;
  }

  private static Condition<ApiErrorException> expectedError(ApiError error) {
    return new Condition<>(
        e -> e.getError().equals(error), "error code %s (%s)", error.getCode(), error.getTitle());
  }
}
