package at.backend.MarketingCompany.crm.deal.domain.exceptions;

public class DealStatusTransitionException extends RuntimeException {
    public DealStatusTransitionException(String message) {
        super(message);
    }
}
