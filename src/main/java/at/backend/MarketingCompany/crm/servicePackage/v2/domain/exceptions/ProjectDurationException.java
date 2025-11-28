package at.backend.MarketingCompany.crm.servicePackage.v2.domain.exceptions;

public class ProjectDurationException extends RuntimeException {
    public ProjectDurationException(String message) {
        super(message);
    }

    public ProjectDurationException(String message, Throwable cause) {
        super(message, cause);
    }
}