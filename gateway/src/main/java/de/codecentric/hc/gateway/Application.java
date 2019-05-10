package de.codecentric.hc.gateway;

import static de.codecentric.hc.gateway.filters.UserIdFilters.REWRITE_TRACK_PATH_WITH_USER_ID;
import static de.codecentric.hc.gateway.filters.UserIdFilters.SET_USER_ID_HEADER;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

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
    return builder
        .routes()
        .route(r -> r.path("/habits/**").uri(habitUri))
        .route(
            r ->
                r.path("/track/**")
                    .filters(f -> f.filter(REWRITE_TRACK_PATH_WITH_USER_ID))
                    .uri(trackUri))
        .route(r -> r.path("/ui/**").uri(uiUri))
        .build();
  }

  @Bean
  public GlobalFilter setUserIdHeader() {
    return SET_USER_ID_HEADER;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
