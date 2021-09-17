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

  protected static final String USER_ID_HEADER_NAME = "X-User-ID";

  public AddRequestHeaderUserId() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) ->
        userId().flatMap(userId -> chain.filter(mutateExchange(exchange, userId)));
  }

  protected ServerWebExchange mutateExchange(ServerWebExchange exchange, String userId) {
    if (StringUtils.isEmpty(userId)) {
      return exchange;
    }
    return exchange.mutate().request(mutateRequest(exchange.getRequest(), userId)).build();
  }

  private ServerHttpRequest mutateRequest(ServerHttpRequest request, String userId) {
    return request.mutate().header(USER_ID_HEADER_NAME, userId).build();
  }

  protected Mono<String> userId() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getName);
  }

  public static class Config {}
}
