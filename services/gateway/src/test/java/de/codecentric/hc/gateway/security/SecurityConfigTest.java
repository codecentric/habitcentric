package de.codecentric.hc.gateway.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import de.codecentric.hc.gateway.security.GatewayAuthConfig.GatewayAuthType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

public class SecurityConfigTest {

  private static final GatewayAuthConfig HTTP_BASIC_CONFIG = new GatewayAuthConfig();
  private static final GatewayAuthConfig OAUTH_2_LOGIN_CONFIG = new GatewayAuthConfig();

  static {
    HTTP_BASIC_CONFIG.setType(GatewayAuthType.HTTP_BASIC);
    OAUTH_2_LOGIN_CONFIG.setType(GatewayAuthType.OAUTH_2_LOGIN);
  }

  private SecurityConfig config;

  @BeforeEach
  public void createConfig() {
    config = new SecurityConfig(OAUTH_2_LOGIN_CONFIG);
  }

  @Test
  public void springSecurityFilterChainShouldNotThrowExceptionWithHttpBasic() throws Exception {
    SecurityWebFilterChain filterChain =
        new SecurityConfig(HTTP_BASIC_CONFIG).springSecurityFilterChain(http());
    assertThat(filterChain).isNotNull();
  }

  @Test
  public void userDetailsServiceShouldHaveExpectedUsers() {
    ReactiveUserDetailsService service = config.userDetailsService();
    assertThat(service.findByUsername("default").block().isEnabled()).isTrue();
    assertThat(service.findByUsername("monitoring").block().isEnabled()).isTrue();
    assertThat(service.findByUsername("user000001").block().isEnabled()).isTrue();
    assertThat(service.findByUsername("user000100").block().isEnabled()).isTrue();
  }

  @Test
  public void passwordEncoderShouldReturnPasswordEncoder() {
    assertThat(config.passwordEncoder()).isNotNull();
  }

  private ServerHttpSecurity http() {
    return ServerHttpSecurity.http()
        .authenticationManager(mock(ReactiveAuthenticationManager.class));
  }
}
