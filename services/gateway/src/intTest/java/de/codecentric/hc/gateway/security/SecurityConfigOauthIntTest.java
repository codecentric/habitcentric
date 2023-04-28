package de.codecentric.hc.gateway.security;

import static de.codecentric.hc.gateway.security.ApplicationUser.Role.USER;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

import de.codecentric.hc.gateway.testing.WebTest;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
public class SecurityConfigOauthIntTest extends WebTest {

  @Autowired ReactiveClientRegistrationRepository repo;

  @Test
  public void requestsWithoutAuthShouldRedirectToOauthLogin() {
    String expectedLocation = "/oauth2/authorization/keycloak";
    get("/actuator")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    get("/habits")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    post("/habits", "{}")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    delete("/habits/123")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    get("/track")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    put("/track", "{}")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    get("/report")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
    get("/ui/overview")
        .expectStatus()
        .isFound()
        .expectHeader()
        .valueMatches("Location", expectedLocation);
  }

  @Test
  public void requestsWithAuthenticatedOauth2UserShouldBeOk() {
    Function<WebTestClient, WebTestClient> customizer =
        webTestClient ->
            webTestClient.mutateWith(
                mockOAuth2Login()
                    .attributes((map) -> map.put("sub", "default"))
                    .authorities(new SimpleGrantedAuthority(String.format("ROLE_%s", USER)))
                    .clientRegistration(repo.findByRegistrationId("keycloak").block()));

    get("/habits", customizer).expectStatus().isOk();
    get("/track/habits/123", customizer).expectStatus().isOk();
    get("/report/achievement", customizer).expectStatus().isOk();
    get("/ui/overview", customizer).expectStatus().isOk();
  }

  @TestConfiguration
  static class MockOAuth2LoginConfiguration {

    /**
     * We need this code as a workaround for the following issue:
     * https://github.com/spring-projects/spring-security/issues/13113
     */
    @Bean
    ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
        ReactiveClientRegistrationRepository clientRegistrationRepository,
        ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
      var delegate =
          new DefaultReactiveOAuth2AuthorizedClientManager(
              clientRegistrationRepository, authorizedClientRepository);
      return new TestReactiveOAuth2AuthorizedClientManager(delegate);
    }
  }

  private static final class TestReactiveOAuth2AuthorizedClientManager
      implements ReactiveOAuth2AuthorizedClientManager {

    static final String TOKEN_ATTR_NAME =
        "org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers$OAuth2ClientMutator$TestReactiveOAuth2AuthorizedClientManager"
            .concat(".TOKEN");

    static final String ENABLED_ATTR_NAME =
        "org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers$OAuth2ClientMutator$TestReactiveOAuth2AuthorizedClientManager"
            .concat(".ENABLED");

    private static final Mono<ServerWebExchange> currentServerWebExchangeMono =
        Mono.deferContextual(Mono::just)
            .filter((c) -> c.hasKey(ServerWebExchange.class))
            .map((c) -> c.get(ServerWebExchange.class));

    private final ReactiveOAuth2AuthorizedClientManager delegate;

    private TestReactiveOAuth2AuthorizedClientManager(
        ReactiveOAuth2AuthorizedClientManager delegate) {
      this.delegate = delegate;
    }

    @Override
    public Mono<OAuth2AuthorizedClient> authorize(OAuth2AuthorizeRequest authorizeRequest) {
      return Mono.justOrEmpty(
              authorizeRequest.<ServerWebExchange>getAttribute(ServerWebExchange.class.getName()))
          .switchIfEmpty(currentServerWebExchangeMono)
          .mapNotNull((exchange) -> exchange.getAttribute(TOKEN_ATTR_NAME));
    }
  }
}
