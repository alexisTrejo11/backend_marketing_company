package at.backend.MarketingCompany.crm.servicePackage.v2.domain.exceptions;

public class ServicePackageValidationException extends RuntimeException {
    public ServicePackageValidationException(String message) {
        super(message);
    }
    
    public ServicePackageValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}