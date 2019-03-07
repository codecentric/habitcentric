package de.codecentric.hc.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.codecentric.hc.gateway.security.ApplicationUserRole.MONITORING;

@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        List<UserDetails> users = Stream.of(ApplicationUser.values()).map(
                user -> User.withDefaultPasswordEncoder()
                        .username(user.getName())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build()).collect(Collectors.toList());
        return new MapReactiveUserDetailsService(users);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/actuator/**").hasRole(MONITORING.name())
                .pathMatchers("/favicon.ico").permitAll()
                .and().httpBasic()
                .and().formLogin();
        return http.build();
    }
}
