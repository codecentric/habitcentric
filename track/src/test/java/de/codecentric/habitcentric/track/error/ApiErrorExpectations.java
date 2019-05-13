package de.codecentric.habitcentric.track.error;

public class ApiErrorExpectations {

  public static final ApiErrorResponse.Error EXPECTED_USER_ID_VIOLATION =
      ApiErrorResponse.Error.builder()
          .code("ConstraintViolation.UserId")
          .detail("must not be blank and size must be between 5 and 64")
          .build();

  public static final ApiErrorResponse.Error EXPECTED_HABIT_ID_VIOLATION =
      ApiErrorResponse.Error.builder()
          .code("ConstraintViolation.HabitId")
          .detail("must be greater than 0")
          .build();
}
