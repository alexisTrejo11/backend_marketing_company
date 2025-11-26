package at.backend.MarketingCompany.crm.deal.v2.domain.exceptions;

public class DealStatusTransitionException extends RuntimeException {
    public DealStatusTransitionException(String message) {
        super(message);
    }
}
