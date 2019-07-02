package de.codecentric.hc.gateway;

import static de.codecentric.hc.gateway.filters.UserIdFilters.REWRITE_TRACK_PATH_WITH_USER_ID;
import static de.codecentric.hc.gateway.filters.UserIdFilters.SET_USER_ID_HEADER;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
        .route(r -> r.path("/habits/**").filters(f -> f.filter(SET_USER_ID_HEADER)).uri(habitUri))
        .route(
            r ->
                r.path("/track/**")
                    .filters(
                        f -> f.filter(REWRITE_TRACK_PATH_WITH_USER_ID).filter(SET_USER_ID_HEADER))
                    .uri(trackUri))
        .route(r -> r.path("/ui/**").filters(f -> f.filter(SET_USER_ID_HEADER)).uri(uiUri))
        .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
