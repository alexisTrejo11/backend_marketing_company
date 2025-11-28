package at.backend.MarketingCompany.crm.opportunity.domain.exceptions;

public class OpportunityValidationException extends RuntimeException {
    public OpportunityValidationException(String message) {
        super(message);
    }
    
    public OpportunityValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
