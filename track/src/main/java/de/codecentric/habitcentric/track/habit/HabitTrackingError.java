package de.codecentric.habitcentric.track.habit;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import de.codecentric.habitcentric.track.error.ApiError;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HabitTrackingError implements ApiError {
  CONSTRAINT_VIOLATION("TRACK_101", "Constraint violation", "{0}", BAD_REQUEST.value());

  private final String code;
  private final String title;
  private final Optional<String> detailTemplate;
  private final int statusCode;

  HabitTrackingError(String code, String title, String detailTemplate, int statusCode) {
    this(code, title, Optional.of(detailTemplate), statusCode);
  }
}
