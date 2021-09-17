package de.codecentric.habitcentric.track.error.dummy;

import de.codecentric.habitcentric.track.error.ApiError;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/** Dummy {@link ApiError} implementation used for testing. */
@Builder
@Getter
@ToString
public class DummyError implements ApiError {

  public static final DummyError DEFAULT =
      DummyError.builder()
          .code("DUMMY_CODE")
          .title("DUMMY_TITLE")
          .detailTemplate(Optional.of("DUMMY_DETAIL"))
          .statusCode(500)
          .build();

  public static final DummyError DEFAULT_WITH_PLACEHOLDER =
      DummyError.builder()
          .code("DUMMY_CODE")
          .title("DUMMY_TITLE")
          .detailTemplate(Optional.of("{1} DUMMY_DETAIL {0}"))
          .statusCode(500)
          .build();

  private final String code;
  private final String title;
  @Builder.Default private final Optional<String> detailTemplate = Optional.empty();
  private final int statusCode;
}
