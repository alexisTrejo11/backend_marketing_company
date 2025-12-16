package at.backend.MarketingCompany.crm.tasks.core.port.input;

import at.backend.MarketingCompany.crm.tasks.core.application.commands.*;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.Task;

public interface TaskCommandService {

    Task createTask(CreateTaskCommand command);

    Task updateTask(UpdateTaskDetailsCommand command);

    Task assingTask(AssignTaskCommand command);

    Task unassignTask(UnassignTaskCommand command);

    Task changeTaskDueDate(ChangeTaskDueDateCommand command);

    Task markTaskInProgressC(MarkTaskInProgressCommand command);

    Task completeTask(CompleteTaskCommand command);

    Task cancelTask(CancelTaskCommand command);

    Task reopenTask(ReopenTaskCommand command);


    void deleteTask(DeleteTaskCommand command);
}
