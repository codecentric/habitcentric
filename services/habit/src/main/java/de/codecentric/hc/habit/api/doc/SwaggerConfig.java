package de.codecentric.hc.habit.api.doc;

import static springfox.documentation.builders.PathSelectors.regex;

import de.codecentric.hc.habit.common.HttpHeaders;
import io.swagger.models.auth.In;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  private static final Set<String> DEFAULT_CONTENT_TYPES =
      Stream.of("application/json").collect(Collectors.toSet());

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "Habit API",
        "Service to manage habits.",
        "",
        "",
        ApiInfo.DEFAULT_CONTACT,
        "",
        "",
        Collections.emptyList());
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("de.codecentric.hc.habit"))
        .paths(regex("/habits.*"))
        .build()
        .apiInfo(apiInfo())
        .consumes(DEFAULT_CONTENT_TYPES)
        .produces(DEFAULT_CONTENT_TYPES)
        .securityContexts(securityContexts())
        .securitySchemes(securitySchemes());
  }

  private List<SecurityContext> securityContexts() {
    List<SecurityContext> securityContexts = new ArrayList<>();
    securityContexts.add(
        SecurityContext.builder()
            .securityReferences(securityReferences())
            .forPaths(PathSelectors.regex("/habits.*"))
            .build());
    return securityContexts;
  }

  private List<SecurityReference> securityReferences() {
    AuthorizationScope[] authorizationScopes = {new AuthorizationScope("global", "Global scope")};
    return List.of(
        new SecurityReference("User-ID", authorizationScopes),
        new SecurityReference("basicAuth", authorizationScopes));
  }

  private List<SecurityScheme> securitySchemes() {
    return List.of(
        new ApiKey("User-ID", HttpHeaders.USER_ID, In.HEADER.name()), new BasicAuth("basicAuth"));
  }
}
