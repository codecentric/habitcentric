package de.codecentric.habitcentric.track.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Provides additional information about problems encountered while performing an operation.
 *
 * @see <a href="https://jsonapi.org/format/#error-objects</a>
 */
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"code", "title", "detail", "id"})
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApiError {

  /** unique identifier for this particular occurrence of the problem */
  @Builder.Default private String id = UUID.randomUUID().toString();

  /** application-specific error code, expressed as a string value */
  private String code;

  /** short, human-readable summary of the problem */
  private String title;

  /** human-readable explanation specific to this occurrence of the problem */
  private String detail;
}
