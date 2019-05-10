package de.codecentric.hc.track.habits.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Positive;

@Constraint(validatedBy = {})
@Positive
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface HabitId {

  String message() default "{javax.validation.constraints.Positive.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
