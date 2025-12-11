package at.backend.MarketingCompany.customer.domain.exceptions;

public class CustomerDomainException extends RuntimeException {
    public CustomerDomainException(String message) {
        super(message);
    }
}
