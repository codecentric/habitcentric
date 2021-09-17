package de.codecentric.hc.habit.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.codecentric.hc.habit.common.HttpHeaders;
import de.codecentric.hc.habit.validation.UserId;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
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
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkZWZhdWx0In0.2E88ZlFE4Tor8d5gRU2451WrLtDavGfgbFf8ZuKBRxM";
    assertThat(
            resolver.resolveArgument(
                mock(MethodParameter.class),
                mock(ModelAndViewContainer.class),
                requestWithAuthorization(authorization),
                mock(WebDataBinderFactory.class)))
        .isEqualTo("default");
  }

  @Test
  public void shouldReturnUserIdWhenAuthorizationIsMissing() {
    String userId = "user123";
    assertThat(
            resolver.resolveArgument(
                mock(MethodParameter.class),
                mock(ModelAndViewContainer.class),
                requestWithUserId(userId),
                mock(WebDataBinderFactory.class)))
        .isEqualTo(userId);
  }

  @Test
  public void shouldThrowAnExceptionWhenTheAuthorizationHeaderIsBasicAuth() {
    String authorization = "Basic dXNlcjoxMjM=";
    assertThat(
            resolver.resolveArgument(
                mock(MethodParameter.class),
                mock(ModelAndViewContainer.class),
                requestWithAuthorization(authorization),
                mock(WebDataBinderFactory.class)))
        .isNull();
  }

  private NativeWebRequest requestWithAuthorization(String headerValue) {
    HttpServletRequest httpRequest = mock(HttpServletRequest.class);
    when(httpRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(headerValue);
    NativeWebRequest webRequest = mock(NativeWebRequest.class);
    when(webRequest.getNativeRequest()).thenReturn(httpRequest);
    return webRequest;
  }

  private NativeWebRequest requestWithUserId(String userId) {
    HttpServletRequest httpRequest = mock(HttpServletRequest.class);
    when(httpRequest.getHeader(HttpHeaders.USER_ID)).thenReturn(userId);
    NativeWebRequest webRequest = mock(NativeWebRequest.class);
    when(webRequest.getNativeRequest()).thenReturn(httpRequest);
    return webRequest;
  }
}
