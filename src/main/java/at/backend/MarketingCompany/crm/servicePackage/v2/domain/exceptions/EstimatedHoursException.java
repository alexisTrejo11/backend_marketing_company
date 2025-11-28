package at.backend.MarketingCompany.crm.servicePackage.v2.domain.exceptions;

public class EstimatedHoursException extends RuntimeException {
    public EstimatedHoursException(String message) {
        super(message);
    }

    public EstimatedHoursException(String message, Throwable cause) {
        super(message, cause);
    }
}