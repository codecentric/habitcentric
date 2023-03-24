package de.codecentric.hc.gateway.security;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("gateway.auth")
@Validated
public class GatewayAuthConfig {

  @Getter @Setter @NotNull private GatewayAuthType type;

  public enum GatewayAuthType {
    HTTP_BASIC,
    OAUTH_2_LOGIN
  }
}
