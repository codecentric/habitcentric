package de.codecentric.habitcentric.track.error;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.habitcentric.track.error.ApiErrorResponse.Error;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ApiErrorResponseMatcher extends TypeSafeMatcher<String> {

  private final List<Error> expected;

  private ApiErrorResponseMatcher(List<Error> expected) {
    this.expected = expected;
  }

  @Override
  protected boolean matchesSafely(String body) {
    try {
      ApiErrorResponse response = new ObjectMapper().readValue(body, ApiErrorResponse.class);
      List<Error> actual = response.getErrors();
      if (!Objects.equals(expected.size(), actual.size())) {
        return false;
      }
      for (int i = 0; i < expected.size(); i++) {
        if (!EqualsBuilder.reflectionEquals(expected.get(i), actual.get(i), "id")) {
          return false;
        }
        if (StringUtils.isBlank(actual.get(i).getId())) {
          return false;
        }
      }
      return true;
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void describeTo(Description description) {
    description.appendText(toString(expected));
  }

  private static String toString(List<Error> errors) {
    return String.format(
        "{\"errors\":[%s]}",
        errors.stream()
            .filter(Objects::nonNull)
            .map(ApiErrorResponseMatcher::toString)
            .collect(Collectors.joining(",")));
  }

  private static String toString(Error error) {
    ToStringBuilder builder = new ToStringBuilder(error, JSON_STYLE);
    if (error.getCode() != null) builder.append("code", error.getCode());
    if (error.getTitle() != null) builder.append("title", error.getTitle());
    if (error.getDetail() != null) builder.append("detail", error.getDetail());
    return builder.build();
  }

  public static Matcher<String> hasApiErrors(Error... expected) {
    return new ApiErrorResponseMatcher(Arrays.asList(expected));
  }
}
