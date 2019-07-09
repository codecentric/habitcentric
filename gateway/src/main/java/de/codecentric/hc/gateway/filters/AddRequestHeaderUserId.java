package de.codecentric.hc.gateway.filters;

import de.codecentric.hc.gateway.filters.AddRequestHeaderUserId.Config;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AddRequestHeaderUserId extends AbstractGatewayFilterFactory<Config> {

  public AddRequestHeaderUserId() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) ->
        userId().flatMap(userId -> chain.filter(mutateRequestWithUserIdHeader(exchange, userId)));
  }

  private ServerWebExchange mutateRequestWithUserIdHeader(
      ServerWebExchange exchange, String userId) {
    if (StringUtils.isEmpty(userId)) {
      return exchange;
    }
    ServerHttpRequest request = exchange.getRequest().mutate().header("X-User-ID", userId).build();
    return exchange.mutate().request(request).build();
  }

  private Mono<String> userId() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getName);
  }

  public static class Config {}
}
