package sg.edu.nus.iss.readingcompanion.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomDateValidator implements ConstraintValidator<ValidDate, String> {

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    
    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null || date.isEmpty()) {
            return true;
        }

        LocalDate now = LocalDate.now();
        LocalDate input = LocalDate.parse(date);

        return input.isBefore(now) || input.isEqual(now);
    }
}
