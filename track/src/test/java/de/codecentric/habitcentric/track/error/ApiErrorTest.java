package de.codecentric.habitcentric.track.error;

import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.habitcentric.track.error.dummy.DummyError;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.springframework.http.HttpStatus;

public class ApiErrorTest {

  private Function<? super Field, Optional<ApiError>> toApiError =
      field -> {
        try {
          return Optional.of((ApiError) field.get(this));
        } catch (IllegalAccessException e) {
          return Optional.empty();
        }
      };

  private final Set<ApiError> errors = getApiErrors();

  @Test
  public void codeShouldNotBeBlank() {
    errors.forEach(error -> assertThat(error.getCode()).isNotBlank());
  }

  @Test
  public void codeShouldBeUnique() {
    Set<String> uniqueCodes = new HashSet<>();
    errors.forEach(
        error ->
            assertThat(uniqueCodes.add(error.getCode()))
                .as("'%s' is not an unique error code", error.getCode()));
  }

  @Test
  public void titleShouldNotBeBlank() {
    errors.forEach(error -> assertThat(error.getTitle()).isNotBlank());
  }

  @Test
  public void titleShouldNotHavePlaceholders() {
    errors.forEach(
        error ->
            assertThat(error.getTitle())
                .as("placeholders (e.g '{0}') in error titles are not supported")
                .doesNotMatch(".*\\{[0-9]+\\}.*"));
  }

  @Test
  public void detailShouldNotBeNull() {
    errors.forEach(error -> assertThat(error.getDetailTemplate()).isNotNull());
  }

  @Test
  public void detailCanBeAnEmptyOptional() {
    ApiError error = DummyError.builder().code("123").title("ABC").statusCode(400).build();
    assertThat(error.getDetailTemplate().isPresent()).isFalse();
    assertThat(error.getDetail("GHI").isPresent()).isFalse();
  }

  @Test
  public void getDetailCanReplaceTemplateProperties() {
    ApiError error =
        DummyError.builder()
            .code("123")
            .title("ABC")
            .detailTemplate(Optional.of("DEF{0}JKL"))
            .statusCode(400)
            .build();
    assertThat(error.getDetail("GHI").get()).isEqualTo("DEFGHIJKL");
  }

  @Test
  public void statusCodeShouldBeValid() {
    errors.forEach(error -> assertThat(HttpStatus.valueOf(error.getStatusCode())).isNotNull());
  }

  private Set<ApiError> getApiErrors() {
    return getApiErrorEnumConstants()
        .map(toApiError)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  private Stream<Field> getApiErrorEnumConstants() {
    return getApiErrorEnums()
        .flatMap(e -> Stream.of(e.getDeclaredFields()))
        .filter(Field::isEnumConstant);
  }

  private Stream<Class<? extends ApiError>> getApiErrorEnums() {
    return new Reflections("de.codecentric.habitcentric")
        .getSubTypesOf(ApiError.class).stream().filter(Class::isEnum);
  }
}
