package de.codecentric.hc.habit.auth;

import de.codecentric.hc.habit.common.HttpHeaders;
import de.codecentric.hc.habit.validation.UserId;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * An implementation of a strategy interface for resolving HTTP headers and converting them into a
 * user ID in the context of a given request. <br>
 * <br>
 * If {@link HttpHeaders#AUTHORIZATION} is present, user ID is extracted from existing JWT. If the
 * header is invalid or missing entirely, {@link HttpHeaders#USER_ID} is used instead.
 */
@Component
public class AuthHeaderArgumentResolver implements HandlerMethodArgumentResolver {
  @Autowired private JwtDecoder jwtDecoder;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(UserId.class) != null;
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {
    HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
    String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
    String userIdHeader = httpRequest.getHeader(HttpHeaders.USER_ID);

    if (authorizationHeader != null && isBearerToken(authorizationHeader)) {
      String token = extractTokenFrom(authorizationHeader);
      Jwt decodedJwt = jwtDecoder.decode(token);
      return getUserIdOf(decodedJwt);
    } else return userIdHeader;
  }

  private boolean isBearerToken(String authorizationHeader) {
    return authorizationHeader.startsWith("Bearer ");
  }

  private String extractTokenFrom(String authorizationHeader) {
    return authorizationHeader.substring(7);
  }

  private String getUserIdOf(Jwt decodedJwt) {
    return decodedJwt.getClaimAsString("sub");
  }
}
