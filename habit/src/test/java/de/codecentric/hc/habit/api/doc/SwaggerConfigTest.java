package de.codecentric.hc.habit.api.doc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfigTest {

  private SwaggerConfig config;

  @Before
  public void beforeEach() {
    config = new SwaggerConfig();
  }

  @Test
  public void apiShouldNotThrowException() {
    Docket docket = config.api();
    assertThat(docket).isNotNull();
  }
}
