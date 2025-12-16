package at.backend.MarketingCompany.crm.tasks.core.application.statistics;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskStatus;
import at.backend.MarketingCompany.crm.tasks.core.application.queries.GetTaskStatisticsQuery;
import at.backend.MarketingCompany.crm.tasks.core.port.output.TaskRepository;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor

public class TaskStatisticService {
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public TaskStatistics getTaskStatistics(GetTaskStatisticsQuery query) {
        log.debug("Fetching task statistics for customer: {}, assignee: {}",
                query.customerCompanyId(), query.assigneeId());

        long totalTasks = 0;
        long pendingTasks = 0;
        long completedTasks = 0;
        long overdueTasks = 0;

        if (query.customerCompanyId() != null) {
            totalTasks = countTasksByCustomer(query.customerCompanyId());
            pendingTasks = taskRepository.countByCustomerAndStatus(query.customerCompanyId(), TaskStatus.PENDING);
            completedTasks = taskRepository.countByCustomerAndStatus(query.customerCompanyId(), TaskStatus.COMPLETED);
        }

        if (query.assigneeId() != null) {
            overdueTasks = taskRepository.countOverdueByAssignee(query.assigneeId());
        }

        return new TaskStatistics(totalTasks, pendingTasks, completedTasks, overdueTasks);
    }

    private long countTasksByCustomer(CustomerCompanyId customerCompanyId) {
        return taskRepository.findByCustomer(customerCompanyId).size();
    }
}
