package at.backend.MarketingCompany.crm.servicePackage.domain.exceptions;

public class PriceException extends RuntimeException {
    public PriceException(String message) {
        super(message);
    }

    public PriceException(String message, Throwable cause) {
        super(message, cause);
    }
}