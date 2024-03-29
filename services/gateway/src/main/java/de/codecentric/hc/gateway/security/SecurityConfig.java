package de.codecentric.hc.gateway.security;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.MONITORING;
import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;

import de.codecentric.hc.gateway.security.GatewayAuthConfig.GatewayAuthType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@AllArgsConstructor
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

  private GatewayAuthConfig authConfig;

  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    List<UserDetails> users =
        Stream.of(ApplicationUser.values())
            .map(
                userDetails ->
                    User.withUsername(userDetails.getUsername())
                        .password(passwordEncoder().encode(userDetails.getPassword()))
                        .roles(userDetails.getRole())
                        .build())
            .collect(Collectors.toList());
    users.addAll(
        ApplicationUser.lptUsers.stream()
            .map(
                username ->
                    User.withUsername(username)
                        .password(passwordEncoder().encode(username))
                        .roles(USER)
                        .build())
            .toList());
    return new MapReactiveUserDetailsService(users);
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http)
      throws Exception {
    configureAuthorization(http);
    return http.build();
  }

  private void configureAuthorization(ServerHttpSecurity http) throws Exception {
    if (GatewayAuthType.OAUTH_2_LOGIN.equals(authConfig.getType())) {
      commonConfig(http).oauth2Login();
    } else {
      commonConfig(http).httpBasic();
    }
  }

  private ServerHttpSecurity commonConfig(ServerHttpSecurity http) throws Exception {
    return http.csrf()
        .disable()
        .authorizeExchange(
            (authorize) ->
                authorize
                    .pathMatchers("/actuator/health/**")
                    .permitAll()
                    .pathMatchers("/actuator/**")
                    .hasRole(MONITORING)
                    .pathMatchers("/favicon.ico")
                    .permitAll()
                    .pathMatchers("/habits/**")
                    .hasRole(USER)
                    .pathMatchers("/track/**")
                    .hasRole(USER)
                    .pathMatchers("/report/**")
                    .hasRole(USER)
                    .pathMatchers("/ui/overview")
                    .hasRole(USER)
                    .pathMatchers("/ui/**")
                    .permitAll()
                    .pathMatchers("/")
                    .permitAll()
                    .and());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> Set.of(new SimpleGrantedAuthority(String.format("ROLE_%s", USER)));
  }
}
