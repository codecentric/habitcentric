package de.codecentric.hc.gateway;

import de.codecentric.hc.gateway.filters.UserIdHeaderFilter;
import de.codecentric.hc.gateway.filters.UserIdTrackPathFilter;
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
  public RouteLocator routeLocator(
      RouteLocatorBuilder builder,
      UserIdHeaderFilter userIdHeaderFilter,
      UserIdTrackPathFilter userIdTrackPathFilter) {
    return builder
        .routes()
        .route(r -> r.path("/habits/**").filters(f -> f.filter(userIdHeaderFilter)).uri(habitUri))
        .route(
            r ->
                r.path("/track/**")
                    .filters(f -> f.filter(userIdTrackPathFilter).filter(userIdHeaderFilter))
                    .uri(trackUri))
        .route(r -> r.path("/ui/**").filters(f -> f.filter(userIdHeaderFilter)).uri(uiUri))
        .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
