package at.backend.MarketingCompany.customer.core.domain.exceptions;

import at.backend.MarketingCompany.shared.exception.DomainException;

public class CustomerDomainException extends DomainException {
    public CustomerDomainException(String message) {
        super(message, "CUSTOMER_ERROR");
    }
}
