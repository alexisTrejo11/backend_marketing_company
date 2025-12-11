package at.backend.MarketingCompany.shared.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class InputValidator {

    private final Validator validator;

    public InputValidator() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public <T> void validate(T input) {
        Set<ConstraintViolation<T>> violations = validator.validate(input);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                sb.append(violation.getMessage()).append("; ");
            }
            throw new ValidationException(sb.toString());
        }
    }
}
