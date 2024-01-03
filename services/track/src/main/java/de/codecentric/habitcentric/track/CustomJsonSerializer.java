package de.codecentric.habitcentric.track;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Custom Spring Kafka {@link JsonSerializer} that serializes timestamps as strings instead of
 * integer arrays.
 *
 * <p>For some reason, Spring Boot does not use the auto-configured {@link ObjectMapper} when
 * auto-configuring Spring Kafka. Instead, it calls the no-args constructor of the {@link
 * JsonSerializer} class provided by the property {@code spring.kafka.producer.value-serializer}.
 *
 * @param <T>
 * @see <a
 *     href="https://github.com/spring-projects/spring-kafka/issues/680">https://github.com/spring-projects/spring-kafka/issues/680</a>
 * @see <a
 *     href="https://docs.spring.io/spring-kafka/reference/tips.html#tip-json">https://docs.spring.io/spring-kafka/reference/tips.html#tip-json</a>
 */
public class CustomJsonSerializer<T> extends JsonSerializer<T> {

  public CustomJsonSerializer() {
    super(createMapper());
  }

  private static ObjectMapper createMapper() {
    var mapper = JacksonUtils.enhancedObjectMapper();
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return mapper;
  }
}
