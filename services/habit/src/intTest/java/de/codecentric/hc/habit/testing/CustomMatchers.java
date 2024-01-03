package de.codecentric.hc.habit.testing;

import java.util.UUID;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatchers {

  public static UuidMatcher isValidUuid() {
    return new UuidMatcher();
  }

  public static class UuidMatcher extends TypeSafeMatcher<String> {
    @Override
    protected boolean matchesSafely(String item) {
      try {
        //noinspection ResultOfMethodCallIgnored
        UUID.fromString(item);
        return true;
      } catch (IllegalArgumentException e) {
        return false;
      }
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("a valid UUID string");
    }

    public static UuidMatcher isValidUuid() {
      return new UuidMatcher();
    }
  }
}
