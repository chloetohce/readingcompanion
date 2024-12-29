package sg.edu.nus.iss.readingcompanion.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageUrlValidator.class)
public @interface ImageUrl {
    String message() default "URL does not return a valid image.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
