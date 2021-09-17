package de.codecentric.hc.habit.api.doc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfigTest {

  private SwaggerConfig config;

  @BeforeEach
  public void beforeEach() {
    config = new SwaggerConfig();
  }

  @Test
  public void apiShouldNotThrowException() {
    Docket docket = config.api();
    assertThat(docket).isNotNull();
  }
}
