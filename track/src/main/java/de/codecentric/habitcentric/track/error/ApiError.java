package de.codecentric.habitcentric.track.error;

import java.text.MessageFormat;
import java.util.Optional;

/** Errors that can be handled by the REST API and should be returned as a response. */
public interface ApiError {

  /**
   * An application-specific error code, expressed as a string value.
   *
   * @return unique identifier used for a given error type
   */
  String getCode();

  /**
   * A short, human-readable summary of the problem that SHOULD NOT change from occurrence to
   * occurrence of the problem, except for purposes of localization.
   *
   * @return simple error description
   */
  String getTitle();

  /**
   * Template for a human-readable explanation specific to this occurrence of the problem. Like
   * title, this field’s value can be localized. May contain placeholders using {@link
   * java.text.MessageFormat}.
   *
   * @see {@link ApiError#getDetail(Object...)}
   * @return template for error details
   */
  Optional<String> getDetailTemplate();

  /**
   * A human-readable explanation specific to this occurrence of the problem. Like title, this
   * field’s value can be localized. Arguments need to be provided when the template contains
   * placeholder.
   *
   * @param templateProperties arguments to replace optional template placeholders
   * @see {@link ApiError#getDetailTemplate()}
   * @return error details
   */
  default Optional<String> getDetail(Object... templateProperties) {
    if (getDetailTemplate().isPresent()) {
      return Optional.of(MessageFormat.format(getDetailTemplate().get(), templateProperties));
    }
    return Optional.empty();
  }

  /** Returns the HTTP status code to use for this error */
  int getStatusCode();
}
