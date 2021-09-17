package de.codecentric.hc.gateway.filters;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserIdMissingException extends ResponseStatusException {

  private static final String REASON = "The required user ID is missing.";

  public UserIdMissingException() {
    super(HttpStatus.UNAUTHORIZED, REASON);
  }
}
