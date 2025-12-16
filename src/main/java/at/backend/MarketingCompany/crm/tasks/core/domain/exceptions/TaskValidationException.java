package at.backend.MarketingCompany.crm.tasks.core.domain.exceptions;

public class TaskValidationException extends RuntimeException {
    public TaskValidationException(String message) {
        super(message);
    }
}
