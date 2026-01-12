package at.backend.MarketingCompany.shared.exception;

import java.util.Map;

public class ValidationException extends DomainException {
    public ValidationException(String message, Map<String, Object> details) {
        super(message, "VALIDATION_ERROR", details);
    }
}
