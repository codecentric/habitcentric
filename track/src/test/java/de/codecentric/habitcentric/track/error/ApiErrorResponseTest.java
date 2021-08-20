package de.codecentric.habitcentric.track.error;

import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.habitcentric.track.error.ApiErrorResponse.Error;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

public class ApiErrorResponseTest {

  private static final Pattern UUID_PATTERN =
      Pattern.compile("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");

  @Test
  public void errorOccurrencesCanBeIdentifiedByAnUniqueId() {
    assertThat(new Error().getId()).matches(UUID_PATTERN);
  }
}
