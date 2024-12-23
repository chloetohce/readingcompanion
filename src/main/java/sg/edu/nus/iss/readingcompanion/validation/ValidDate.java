package sg.edu.nus.iss.readingcompanion.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomDateValidator.class)
public @interface ValidDate {
    String message() default "Date must be in the past or present.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
