package de.codecentric.hc.gateway.filters;

import de.codecentric.hc.gateway.filters.RewriteTrackPathWithUserId.Config;
import java.nio.charset.StandardCharsets;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
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
public class RewriteTrackPathWithUserId extends AbstractGatewayFilterFactory<Config> {

  public RewriteTrackPathWithUserId() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) ->
        userId().flatMap(userId -> chain.filter(mutateExchange(exchange, userId)));
  }

  protected ServerWebExchange mutateExchange(ServerWebExchange exchange, String userId) {
    if (StringUtils.isEmpty(userId)) {
      throw new UserIdMissingException();
    }
    return exchange.mutate().request(mutateRequest(exchange.getRequest(), userId)).build();
  }

  private ServerHttpRequest mutateRequest(ServerHttpRequest request, String userId) {
    return request.mutate().path(mutatePath(request.getPath().value(), userId)).build();
  }

  private String mutatePath(String path, String userId) {
    return path.replaceFirst("/track/", trackPathWithUserId(userId));
  }

  private String trackPathWithUserId(String userId) {
    return String.format(
        "/track/users/%s/", UriUtils.encodePath(userId, StandardCharsets.UTF_8.toString()));
  }

  protected Mono<String> userId() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getName);
  }

  public static class Config {}
}
