package at.backend.MarketingCompany.crm.quote.domain.exception;

import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;

public class QuoteNotFoundException extends RuntimeException {
    public QuoteNotFoundException(String message) {
        super(message);
    }
    
    public QuoteNotFoundException(QuoteId id) {
        super("Quote not found with ID: " + id.value());
    }
}