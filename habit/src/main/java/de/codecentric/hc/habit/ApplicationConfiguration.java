package de.codecentric.hc.habit;

import de.codecentric.hc.habit.jwt.InsecureJwtDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

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
