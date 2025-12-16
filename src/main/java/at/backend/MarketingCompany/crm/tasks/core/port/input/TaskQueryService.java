package at.backend.MarketingCompany.crm.tasks.core.port.input;


import at.backend.MarketingCompany.crm.tasks.core.application.queries.*;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.Task;
import org.springframework.data.domain.Page;

public interface TaskQueryService {

    Task getTaskById(GetTaskByIdQuery query);

    Page<Task> getTasksByStatus(GetTasksByStatusQuery query);

    Page<Task> getTasksByCustomer(GetTasksByCustomerQuery query);

    Page<Task> getTasksByOpportunity(GetTasksByOpportunityQuery query);

    Page<Task> getTasksByAssignee(GetTasksByAssigneeQuery query);

    Page<Task> getTasksByPriority(GetTasksByPriorityQuery query);

    Page<Task> getOverdueTasks(GetOverdueTasksQuery query);

    Page<Task> getPendingTasks(GetPendingTasksQuery query);

    Page<Task> searchTasks(SearchTasksQuery query);
}
