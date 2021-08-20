package de.codecentric.habitcentric.track;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import de.codecentric.habitcentric.track.auth.InsecureJwtDecoder;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

public class ApplicationConfigurationTest {

  private ApplicationConfiguration config = new ApplicationConfiguration();

  @Test
  public void shouldUseFixedLocaleUs() {
    assertThat(config.localeResolver().resolveLocale(mock(HttpServletRequest.class)))
        .isEqualTo(Locale.US);
  }

  @Test
  public void shouldUseInsecureJwtDecoder() {
    assertThat(config.jwtDecoder()).isInstanceOf(InsecureJwtDecoder.class);
  }
}
