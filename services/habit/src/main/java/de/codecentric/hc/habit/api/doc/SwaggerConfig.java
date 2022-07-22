package de.codecentric.hc.habit.api.doc;

import de.codecentric.hc.habit.common.HttpHeaders;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  private static final Set<String> DEFAULT_CONTENT_TYPES =
      Stream.of("application/json").collect(Collectors.toSet());

  @Bean
  OpenAPI apiInfo() {
    return new OpenAPI()
        .info(new Info().title("Habit API").description("Service to manage habits."))
        .schemaRequirement(
            "User-ID",
            new SecurityScheme().type(Type.APIKEY).in(In.HEADER).name(HttpHeaders.USER_ID))
        .schemaRequirement("basicAuth", new SecurityScheme().type(Type.HTTP).scheme("Basic"));
  }
}
