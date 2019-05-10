package de.codecentric.hc.gateway.security;

import static de.codecentric.hc.gateway.security.ApplicationUserRole.MONITORING;
import static de.codecentric.hc.gateway.security.ApplicationUserRole.USER;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    List<UserDetails> users =
        Stream.of(ApplicationUser.values())
            .map(
                userDetails ->
                    User.withUsername(userDetails.getUsername())
                        .password(passwordEncoder().encode(userDetails.getPassword()))
                        .roles(userDetails.getRole().name())
                        .build())
            .collect(Collectors.toList());
    users.addAll(
        ApplicationUser.lptUsers.stream()
            .map(
                username ->
                    User.withUsername(username)
                        .password(passwordEncoder().encode(username))
                        .roles(USER.name())
                        .build())
            .collect(Collectors.toList()));
    return new MapReactiveUserDetailsService(users);
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf()
        .disable()
        .authorizeExchange()
        .pathMatchers("/actuator/health/**")
        .permitAll()
        .pathMatchers("/actuator/**")
        .hasRole(MONITORING.name())
        .pathMatchers("/favicon.ico")
        .permitAll()
        .pathMatchers("/habits/**")
        .hasRole(USER.name())
        .pathMatchers("/track/**")
        .hasRole(USER.name())
        .pathMatchers("/ui/**")
        .hasRole(USER.name())
        .and()
        .httpBasic();
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
