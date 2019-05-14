package de.codecentric.habitcentric.track.error.matcher;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.habitcentric.track.error.ApiError;
import de.codecentric.habitcentric.track.error.ApiErrorResponse;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ApiErrorMatcher extends TypeSafeMatcher<String> {

  private final String expectedCode;
  private final String expectedTitle;
  private final String expectedDetail;

  private ApiErrorMatcher(ApiError expected, Object... templateProperties) {
    this.expectedCode = expected.getCode();
    this.expectedTitle = expected.getTitle();
    this.expectedDetail = expected.getDetail(templateProperties).orElse(null);
  }

  @Override
  protected boolean matchesSafely(String body) {
    return parseResponse(body).getErrors().stream()
        .filter(e -> Objects.equals(expectedCode, e.getCode()))
        .filter(e -> Objects.equals(expectedTitle, e.getTitle()))
        .filter(e -> Objects.equals(expectedDetail, e.getDetail()))
        .findFirst()
        .isPresent();
  }

  private ApiErrorResponse parseResponse(String body) {
    try {
      return new ObjectMapper().readValue(body, ApiErrorResponse.class);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void describeTo(Description description) {
    ToStringBuilder builder = new ToStringBuilder(this, JSON_STYLE);
    if (expectedCode != null) builder.append("code", expectedCode);
    if (expectedTitle != null) builder.append("title", expectedTitle);
    if (expectedDetail != null) builder.append("detail", expectedDetail);
    description.appendText(builder.build());
  }

  public static Matcher<String> hasError(ApiError expected, Object... templateProperties) {
    return new ApiErrorMatcher(expected, templateProperties);
  }
}
