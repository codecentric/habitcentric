package de.codecentric.hc.gateway.testing;

import de.codecentric.hc.gateway.security.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.util.Base64Utils;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class WebTest extends GatewayTest {

    @Autowired
    protected WebTestClient webClient;

    protected ResponseSpec get(String uri) {
        return webClient.get().uri(uri).exchange();
    }

    protected ResponseSpec get(String uri, ApplicationUser user) {
        return webClient.get().uri(uri)
                .header("Authorization", basicAuthHeader(user)).exchange();
    }

    private String basicAuthHeader(ApplicationUser user) {
        return String.format("Basic %s", base64Credentials(user));
    }

    private String base64Credentials(ApplicationUser user) {
        return Base64Utils.encodeToString((plainCredentials(user)).getBytes(UTF_8));
    }

    private String plainCredentials(ApplicationUser user) {
        return String.format("%s:%s", user.getName(), user.getPassword());
    }
}
