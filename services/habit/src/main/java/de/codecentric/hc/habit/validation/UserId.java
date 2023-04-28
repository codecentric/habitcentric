package de.codecentric.hc.habit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
