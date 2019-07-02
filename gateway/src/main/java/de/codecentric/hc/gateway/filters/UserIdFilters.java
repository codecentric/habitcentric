package de.codecentric.hc.gateway.filters;

import java.nio.charset.StandardCharsets;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

public final class UserIdFilters {

  public static GatewayFilter REWRITE_TRACK_PATH_WITH_USER_ID =
      (exchange, chain) ->
          userId()
              .flatMap(userId -> chain.filter(mutateTrackRequestWithUserIdPath(exchange, userId)));

  private static ServerWebExchange mutateTrackRequestWithUserIdPath(
      ServerWebExchange exchange, String userId) {
    if (StringUtils.isEmpty(userId)) {
      throw new UserIdMissingException();
    }
    ServerHttpRequest request =
        exchange
            .getRequest()
            .mutate()
            .path(
                exchange
                    .getRequest()
                    .getPath()
                    .value()
                    .replaceFirst("/track/", trackPathWithUserId(userId)))
            .build();
    return exchange.mutate().request(request).build();
  }

  private static String trackPathWithUserId(String userId) {
    return String.format(
        "/track/users/%s/", UriUtils.encodePath(userId, StandardCharsets.UTF_8.toString()));
  }

  public static GatewayFilter SET_USER_ID_HEADER =
      (exchange, chain) ->
          userId().flatMap(userId -> chain.filter(mutateRequestWithUserIdHeader(exchange, userId)));

  private static ServerWebExchange mutateRequestWithUserIdHeader(
      ServerWebExchange exchange, String userId) {
    if (StringUtils.isEmpty(userId)) {
      return exchange;
    }
    ServerHttpRequest request =
        exchange
            .getRequest()
            .mutate()
            .header("X-User-ID", userId)
            .headers(httpHeaders -> httpHeaders.remove("Authorization"))
            .build();
    return exchange.mutate().request(request).build();
  }

  private static Mono<String> userId() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getName);
  }
}
