package at.backend.MarketingCompany.crm.tasks.core.domain.exceptions;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException(TaskId taskId) {
        super("Task", taskId.asString());
    }
}
