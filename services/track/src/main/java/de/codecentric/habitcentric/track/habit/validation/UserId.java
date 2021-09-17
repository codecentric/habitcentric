package de.codecentric.habitcentric.track.habit.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Constraint(validatedBy = {})
@NotBlank
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 5, max = 64)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface UserId {

  String message() default "must not be blank and size must be between 5 and 64";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
