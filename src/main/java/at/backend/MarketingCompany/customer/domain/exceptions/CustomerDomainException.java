package at.backend.MarketingCompany.customer.domain.exceptions;

import at.backend.MarketingCompany.shared.exception.DomainException;

public class CustomerDomainException extends DomainException {
    public CustomerDomainException(String message) {
        super(message, "CUSTOMER_ERROR");
    }
}
