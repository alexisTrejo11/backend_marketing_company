package at.backend.MarketingCompany.crm.tasks.domain.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
