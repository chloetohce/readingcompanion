package sg.edu.nus.iss.readingcompanion.validation;

import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageUrlValidator implements ConstraintValidator<ImageUrl, String> {

    @Override
    public void initialize(ImageUrl constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().equals("")) {
            return true;
        }
        
        URLValidator urlValidator = new URLValidator();
        if (urlValidator.isValid(value, context)) {
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> requestEntity = RequestEntity.get(value)
            .build();
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
        MediaType contentType = response.getHeaders().getContentType();
        MediaType imageType = MediaType.valueOf("image/*");
        if (contentType.isCompatibleWith(imageType)) {
            return true;
        }
        return false;
    }
    
}
