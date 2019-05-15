package de.codecentric.habitcentric.track.auth;

import static de.codecentric.habitcentric.track.auth.AuthError.JWT_TOKEN_DECODING_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import de.codecentric.habitcentric.track.error.ApiError;
import de.codecentric.habitcentric.track.error.ApiErrorException;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public class InsecureJwtDecoderTest {

  private JwtDecoder jwtDecoder;

  @Before
  public void setup() {
    this.jwtDecoder = new InsecureJwtDecoder();
  }

  @Test
  public void decode_whenValidToken_thenSuccess() {
    String insertedToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.5mhBHqs5_DTLdINd9p5m7ZJ6XD0Xc55kIaCRY5r6HRA";
    Map<String, Object> expectedHeaders = expectedHeaders();
    Map<String, Object> expectedClaims = expectedClaims();

    Jwt jwt = jwtDecoder.decode(insertedToken);

    assertThat(jwt.getHeaders()).containsAllEntriesOf(expectedHeaders);
    assertThat(jwt.getClaims()).containsAllEntriesOf(expectedClaims);
  }

  @Test
  public void decode_whenInvalidSignatureToken_thenSuccess() {
    String insertedToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
            + ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.sfddsf";
    Map<String, Object> expectedHeaders = expectedHeaders();
    Map<String, Object> expectedClaims = expectedClaims();

    Jwt jwt = jwtDecoder.decode(insertedToken);

    assertThat(jwt.getHeaders()).containsAllEntriesOf(expectedHeaders);
    assertThat(jwt.getClaims()).containsAllEntriesOf(expectedClaims);
  }

  private Map<String, Object> expectedHeaders() {
    Map<String, Object> expectedHeaders = new HashMap<>();
    expectedHeaders.put("alg", "HS256");
    expectedHeaders.put("typ", "JWT");
    return expectedHeaders;
  }

  private Map<String, Object> expectedClaims() {
    Map<String, Object> expectedClaims = new LinkedHashMap<>();
    expectedClaims.put("name", "John Doe");
    expectedClaims.put("sub", "1234567890");
    expectedClaims.put("iat", Instant.ofEpochSecond(1516239022));
    return expectedClaims;
  }

  @Test
  public void decode_whenMalformedHeaderToken_thenJwtException() {
    String malformedHeaderToken =
        "dfsdf.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
            + ".5mhBHqs5_DTLdINd9p5m7ZJ6XD0Xc55kIaCRY5r6HRA";

    assertThatExceptionOfType(ApiErrorException.class)
        .isThrownBy(() -> jwtDecoder.decode(malformedHeaderToken))
        .has(expectedError(JWT_TOKEN_DECODING_ERROR));
  }

  @Test
  public void decode_whenMalformedPayloadToken_thenJwtException() {
    String malformedPayloadToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fdsfsd"
            + ".MZZ7UbJRJH9hFRdBUQHpMjU4TK4XRrYP5UxcAkEHvxE";

    assertThatExceptionOfType(ApiErrorException.class)
        .isThrownBy(() -> jwtDecoder.decode(malformedPayloadToken))
        .has(expectedError(JWT_TOKEN_DECODING_ERROR));
  }

  private static Condition<ApiErrorException> expectedError(ApiError error) {
    return new Condition<>(
        e -> e.getError().equals(error), "error code %s (%s)", error.getCode(), error.getTitle());
  }
}
