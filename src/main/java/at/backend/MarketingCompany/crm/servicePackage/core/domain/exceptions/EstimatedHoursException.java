package at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions;

public class EstimatedHoursException extends RuntimeException {
    public EstimatedHoursException(String message) {
        super(message);
    }

    public EstimatedHoursException(String message, Throwable cause) {
        super(message, cause);
    }
}