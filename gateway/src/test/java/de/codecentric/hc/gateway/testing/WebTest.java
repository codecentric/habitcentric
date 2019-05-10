package de.codecentric.hc.gateway.testing;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import de.codecentric.hc.gateway.security.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.util.Base64Utils;

public abstract class WebTest extends GatewayTest {

  @Autowired protected WebTestClient webClient;

  protected ResponseSpec get(String uri) {
    return webClient.get().uri(uri).exchange();
  }

  protected ResponseSpec get(String uri, ApplicationUser user) {
    return webClient.get().uri(uri).header("Authorization", basicAuthHeader(user)).exchange();
  }

  protected ResponseSpec post(String uri, Object body) {
    return webClient.post().uri(uri).contentType(APPLICATION_JSON_UTF8).syncBody(body).exchange();
  }

  protected ResponseSpec post(String uri, ApplicationUser user, Object body) {
    return webClient
        .post()
        .uri(uri)
        .header("Authorization", basicAuthHeader(user))
        .contentType(APPLICATION_JSON_UTF8)
        .syncBody(body)
        .exchange();
  }

  protected ResponseSpec put(String uri, Object body) {
    return webClient.put().uri(uri).contentType(APPLICATION_JSON_UTF8).syncBody(body).exchange();
  }

  protected ResponseSpec put(String uri, ApplicationUser user, Object body) {
    return webClient
        .put()
        .uri(uri)
        .header("Authorization", basicAuthHeader(user))
        .contentType(APPLICATION_JSON_UTF8)
        .syncBody(body)
        .exchange();
  }

  protected ResponseSpec delete(String uri) {
    return webClient.delete().uri(uri).exchange();
  }

  protected ResponseSpec delete(String uri, ApplicationUser user) {
    return webClient.delete().uri(uri).header("Authorization", basicAuthHeader(user)).exchange();
  }

  private String basicAuthHeader(ApplicationUser user) {
    return String.format("Basic %s", base64Credentials(user));
  }

  private String base64Credentials(ApplicationUser user) {
    return Base64Utils.encodeToString((plainCredentials(user)).getBytes(UTF_8));
  }

  private String plainCredentials(ApplicationUser user) {
    return String.format("%s:%s", user.getUsername(), user.getPassword());
  }
}
