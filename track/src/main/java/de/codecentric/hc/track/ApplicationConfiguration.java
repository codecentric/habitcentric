package de.codecentric.hc.track;

import de.codecentric.hc.track.jwt.AuthHeaderArgumentResolver;
import de.codecentric.hc.track.jwt.InsecureJwtDecoder;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

  @Autowired private AuthHeaderArgumentResolver authHeaderArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(authHeaderArgumentResolver);
  }

  @Bean
  public LocaleResolver localeResolver() {
    return new FixedLocaleResolver(Locale.US);
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return new InsecureJwtDecoder();
  }
}
