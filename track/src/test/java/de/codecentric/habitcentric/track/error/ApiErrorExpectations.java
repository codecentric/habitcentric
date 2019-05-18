package de.codecentric.habitcentric.track.error;

public class ApiErrorExpectations {

  public static final ApiError EXPECTED_USER_ID_VIOLATION =
      ApiError.builder()
          .code("ConstraintViolation.UserId")
          .detail("must not be blank and size must be between 5 and 64")
          .build();

  public static final ApiError EXPECTED_HABIT_ID_VIOLATION =
      ApiError.builder()
          .code("ConstraintViolation.HabitId")
          .detail("must be greater than 0")
          .build();
}
