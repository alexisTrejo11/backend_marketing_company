package at.backend.MarketingCompany.crm.interaction.domain.exceptions;

public class InteractionValidationException extends RuntimeException {
    public InteractionValidationException(String message) {
        super(message);
    }
    
    public InteractionValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}