package de.codecentric.hc.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

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
                .route(r -> r.path("/habits/**")
                        .filters(removeAuthorizationHeader)
                        .uri(habitUri))
                .route(r -> r.path("/track/**")
                        .filters(removeAuthorizationHeader)
                        .uri(trackUri))
                .route(r -> r.path("/ui/**")
                        .filters(removeAuthorizationHeader)
                        .uri(uiUri))
                .build();
    }

    private Function<GatewayFilterSpec, UriSpec> removeAuthorizationHeader = f -> f.removeRequestHeader("Authorization");

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
