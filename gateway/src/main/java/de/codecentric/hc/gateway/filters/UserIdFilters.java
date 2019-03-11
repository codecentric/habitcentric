package de.codecentric.hc.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Base64Utils;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UserIdFilters {

    public static GatewayFilter REWRITE_TRACK_PATH_WITH_USER_ID = (exchange, chain) -> {
        String userId = extractUserId(exchange.getRequest());
        String replacement = String.format("/track/users/%s/", UriUtils.encodePath(userId, StandardCharsets.UTF_8.toString()));
        ServerHttpRequest request = exchange.getRequest().mutate()
                .path(exchange.getRequest().getPath().value().replaceFirst("/track/", replacement))
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    };

    public static GlobalFilter SET_USER_ID_HEADER = (exchange, chain) -> {
        String userId = extractUserId(exchange.getRequest());
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-User-ID", userId)
                .headers(httpHeaders -> httpHeaders.remove("Authorization"))
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    };

    private static String extractUserId(ServerHttpRequest request) {
        String auth = request.getHeaders().getFirst("Authorization");
        String base64Credentials = auth.replace("Basic ", "");
        return extractUserId(new String(Base64Utils.decodeFromString(base64Credentials)));
    }

    private static final Pattern plainCredentialsPattern = Pattern.compile("(.*):(.*)");

    private static String extractUserId(String plainCredentials) {
        Matcher matcher = plainCredentialsPattern.matcher(plainCredentials);
        matcher.find();
        return matcher.group(1);
    }
}
