package de.codecentric.hc.gateway.testing;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

public abstract class WebTest extends GatewayTest {

  @Autowired protected WebTestClient webClient;

  protected ResponseSpec get(String uri) {
    return webClient.get().uri(uri).exchange();
  }

  protected ResponseSpec get(
      String uri, Function<WebTestClient, WebTestClient> webTestClientCustomizer) {
    return webTestClientCustomizer.apply(webClient).get().uri(uri).exchange();
  }

  protected ResponseSpec post(String uri, Object body) {
    return webClient.post().uri(uri).contentType(APPLICATION_JSON).bodyValue(body).exchange();
  }

  protected ResponseSpec put(String uri, Object body) {
    return webClient.put().uri(uri).contentType(APPLICATION_JSON).bodyValue(body).exchange();
  }

  protected ResponseSpec delete(String uri) {
    return webClient.delete().uri(uri).exchange();
  }
}
