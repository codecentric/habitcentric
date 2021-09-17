package de.codecentric.hc.gateway.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

public class RewriteTrackPathWithUserIdTest {

  private RewriteTrackPathWithUserId filterFactory;
  private GatewayFilter filter;
  private MockServerWebExchange exchange;

  @BeforeEach
  public void beforeEach() {
    filterFactory = new RewriteTrackPathWithUserId();
    filter = filterFactory.apply(new RewriteTrackPathWithUserId.Config());
    exchange = MockServerWebExchange.from(get("/track/habits"));
  }

  @Test
  public void shouldCreateFilterInstance() {
    assertThat(filter).isNotNull();
  }

  @DisplayName("mutateExchange should rewrite path to")
  @ParameterizedTest(name = "{1} when userId is {0}")
  @CsvSource({"default,/track/users/default/habits", "?=&#+,/track/users/%3F=&%23+/habits"})
  public void mutateExchangeShouldRewritePathWithUserId(String userId, String expectedPath) {
    ServerWebExchange mutatedExchange = filterFactory.mutateExchange(exchange, userId);
    assertThat(mutatedExchange.getRequest().getPath().value()).isEqualTo(expectedPath);
  }

  @DisplayName("mutateExchange should throw an UserIdMissingException when userId is empty")
  @ParameterizedTest
  @NullAndEmptySource
  public void shouldReturnUnmodifiedExchangeWhenUserIdIsEmpty(String userId) {
    UserIdMissingException expected =
        assertThrows(
            UserIdMissingException.class, () -> filterFactory.mutateExchange(exchange, userId));
    assertThat(expected).hasMessage("401 UNAUTHORIZED \"The required user ID is missing.\"");
  }

  @Test
  public void userIdShouldBeNullWithoutSecurityContext() {
    assertThat(filterFactory.userId().block()).isNull();
  }
}
