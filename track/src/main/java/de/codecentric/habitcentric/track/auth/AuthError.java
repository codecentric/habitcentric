package de.codecentric.habitcentric.track.auth;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import de.codecentric.habitcentric.track.error.ApiError;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthError implements ApiError {
  JWT_TOKEN_DECODING_ERROR(
      "TRACK_102",
      "JWT token could not be decoded",
      "An error occurred while attempting to decode the Jwt: {0}",
      BAD_REQUEST.value()),
  JWT_TOKEN_MISSING(
      "TRACK_100",
      "JWT token missing",
      "The JWT token required for this operation is missing.",
      UNAUTHORIZED.value());

  private final String code;
  private final String title;
  private final Optional<String> detailTemplate;
  private final int statusCode;

  AuthError(String code, String title, String detailTemplate, int statusCode) {
    this(code, title, Optional.of(detailTemplate), statusCode);
  }
}
