package de.codecentric.hc.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Base64Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class Application {

    @Value("${gateway.target.uri.habit}")
    private String habitUri;

    @Value("${gateway.target.uri.track}")
    private String trackUri;

    @Value("${gateway.target.uri.ui}")
    private String uiUri;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/habits/**").uri(habitUri))
                .route(r -> r.path("/track/**").uri(trackUri))
                .route(r -> r.path("/ui/**").uri(uiUri))
                .build();
    }

    @Bean
    public GlobalFilter setUserIdHeader() {
        return (exchange, chain) -> {
            String userId = extractUserId(exchange.getRequest());
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-User-ID", userId)
                    .headers(httpHeaders -> httpHeaders.remove("Authorization"))
                    .build();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    protected String extractUserId(ServerHttpRequest request) {
        String auth = request.getHeaders().getFirst("Authorization");
        String base64Credentials = auth.replace("Basic ", "");
        return extractUserId(new String(Base64Utils.decodeFromString(base64Credentials)));
    }

    private final Pattern plainCredentialsPattern = Pattern.compile("(.*):(.*)");

    protected String extractUserId(String plainCredentials) {
        Matcher matcher = plainCredentialsPattern.matcher(plainCredentials);
        matcher.find();
        return matcher.group(1);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
