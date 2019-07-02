package de.codecentric.hc.gateway.filters;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

public final class UserIdFilters {

  public static GatewayFilter REWRITE_TRACK_PATH_WITH_USER_ID =
      (exchange, chain) -> {
        Optional<String> userId = extractUserId(exchange.getRequest());
        String replacement =
            String.format(
                "/track/users/%s/",
                UriUtils.encodePath(
                    userId.orElseThrow(() -> new UserIdMissingException()),
                    StandardCharsets.UTF_8.toString()));
        ServerHttpRequest request =
            exchange
                .getRequest()
                .mutate()
                .path(exchange.getRequest().getPath().value().replaceFirst("/track/", replacement))
                .build();
        return chain.filter(exchange.mutate().request(request).build());
      };

  public static GatewayFilter SET_USER_ID_HEADER =
      (exchange, chain) -> {
        Optional<String> userId = extractUserId(exchange.getRequest());
        if (!userId.isPresent()) {
          return chain.filter(exchange);
        }
        ServerHttpRequest request =
            exchange
                .getRequest()
                .mutate()
                .header("X-User-ID", userId.get())
                .headers(httpHeaders -> httpHeaders.remove("Authorization"))
                .build();
        return chain.filter(exchange.mutate().request(request).build());
      };

  private static Optional<String> extractUserId(ServerHttpRequest request) {
    String auth = request.getHeaders().getFirst("Authorization");
    if (StringUtils.isEmpty(auth)) {
      return Optional.empty();
    }
    String base64Credentials = auth.replace("Basic ", "");
    return extractUserId(new String(Base64Utils.decodeFromString(base64Credentials)));
  }

  private static final Pattern plainCredentialsPattern = Pattern.compile("(.*):(.*)");

  private static Optional<String> extractUserId(String plainCredentials) {
    Matcher matcher = plainCredentialsPattern.matcher(plainCredentials);
    return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
  }
}
