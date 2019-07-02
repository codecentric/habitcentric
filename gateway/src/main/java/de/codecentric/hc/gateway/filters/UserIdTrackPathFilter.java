package de.codecentric.hc.gateway.filters;

import java.nio.charset.StandardCharsets;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

@Component
public class UserIdTrackPathFilter implements GatewayFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return userId()
        .flatMap(userId -> chain.filter(mutateTrackRequestWithUserIdPath(exchange, userId)));
  }

  private ServerWebExchange mutateTrackRequestWithUserIdPath(
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

  private String trackPathWithUserId(String userId) {
    return String.format(
        "/track/users/%s/", UriUtils.encodePath(userId, StandardCharsets.UTF_8.toString()));
  }

  private Mono<String> userId() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getName);
  }
}
