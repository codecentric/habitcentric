package de.codecentric.habitcentric.track;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CustomJsonSerializerTest {

  @Test
  void creates() {
    try (var subject = new CustomJsonSerializer<>()) {
      assertThat(subject).isNotNull();
    }
  }
}
