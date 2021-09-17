package de.codecentric.habitcentric.track.error;

import java.util.Optional;
import lombok.Getter;

@Getter
public abstract class ApiErrorException extends RuntimeException {

  private final ApiError error;
  private final Object[] templateProperties;

  public ApiErrorException(ApiError error, Throwable cause, Object... templateProperties) {
    super(toMessage(error, templateProperties), cause);
    this.error = error;
    this.templateProperties = templateProperties;
  }

  public ApiErrorException(ApiError error, Object... templateProperties) {
    super(toMessage(error, templateProperties));
    this.error = error;
    this.templateProperties = templateProperties;
  }

  private static String toMessage(ApiError error, Object... templateProperties) {

    StringBuilder builder = new StringBuilder(error.getTitle());

    Optional<String> detail = error.getDetail(templateProperties);

    if (detail.isPresent()) {
      builder.append(" - ").append(detail.get());
    }

    return builder.append(" [").append(error.getCode()).append("]").toString();
  }
}
