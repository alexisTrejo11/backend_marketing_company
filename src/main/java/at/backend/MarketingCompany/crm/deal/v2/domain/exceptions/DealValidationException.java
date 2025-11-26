package at.backend.MarketingCompany.crm.deal.v2.domain.exceptions;

public class DealValidationException extends RuntimeException {
    public DealValidationException(String message) {
        super(message);
    }
}
