package at.backend.MarketingCompany.crm.tasks.domain.exceptions;

public class TaskValidationException extends RuntimeException {
    public TaskValidationException(String message) {
        super(message);
    }
}
