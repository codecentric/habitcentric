package de.codecentric.hc.habit;

import de.codecentric.hc.habit.auth.InsecureJwtDecoder;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public LocaleResolver localeResolver() {
    return new FixedLocaleResolver(Locale.US);
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return new InsecureJwtDecoder();
  }
}
