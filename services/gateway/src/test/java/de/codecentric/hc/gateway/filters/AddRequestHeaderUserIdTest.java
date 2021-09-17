package de.codecentric.hc.gateway.filters;

import static de.codecentric.hc.gateway.filters.AddRequestHeaderUserId.USER_ID_HEADER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

public class AddRequestHeaderUserIdTest {

  private AddRequestHeaderUserId filterFactory;
  private GatewayFilter filter;
  private MockServerWebExchange exchange;

  @BeforeEach
  public void beforeEach() {
    filterFactory = new AddRequestHeaderUserId();
    filter = filterFactory.apply(new AddRequestHeaderUserId.Config());
    exchange = MockServerWebExchange.from(get("/habits"));
  }

  @Test
  public void shouldCreateFilterInstance() {
    assertThat(filter).isNotNull();
  }

  @DisplayName("mutateExchange should set the userId as header")
  @Test
  public void mutatedExchangeShouldSetUserIdAsHeader() {
    String userId = "default";
    ServerWebExchange mutatedExchange = filterFactory.mutateExchange(exchange, userId);
    assertThat(mutatedExchange.getRequest().getHeaders().get(USER_ID_HEADER_NAME))
        .containsExactly(userId);
  }

  @DisplayName("mutateExchange should not modify the exchange when userId is empty")
  @ParameterizedTest
  @NullAndEmptySource
  public void mutateExchangeShouldReturnUnmodifiedExchangeWhenUserIdIsEmpty(String userId) {
    assertThat(filterFactory.mutateExchange(exchange, userId)).isSameAs(exchange);
  }

  @Test
  public void userIdShouldBeNullWithoutSecurityContext() {
    assertThat(filterFactory.userId().block()).isNull();
  }
}
