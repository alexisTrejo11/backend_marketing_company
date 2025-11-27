package at.backend.MarketingCompany.crm.deal.domain.exceptions;

public class DealValidationException extends RuntimeException {
    public DealValidationException(String message) {
        super(message);
    }
}
