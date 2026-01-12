package at.backend.MarketingCompany.crm.tasks.core.application;

import at.backend.MarketingCompany.crm.tasks.core.application.queries.*;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.Task;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;
import at.backend.MarketingCompany.crm.tasks.core.domain.exceptions.TaskNotFoundException;
import at.backend.MarketingCompany.crm.tasks.core.port.output.TaskRepository;
import at.backend.MarketingCompany.crm.tasks.core.port.input.TaskQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskQueryServiceImpl implements TaskQueryService {
    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public Task getTaskById(GetTaskByIdQuery query) {
        log.debug("Fetching task by ID: {}", query.taskId());

        return taskRepository.findById(query.taskId())
                .orElseThrow(() -> new TaskNotFoundException(query.taskId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getTasksByStatus(GetTasksByStatusQuery query) {
        log.debug("Fetching tasks by status: {}", query.statuses());

        return taskRepository.findByStatuses(query.statuses(), query.pageable());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getTasksByCustomer(GetTasksByCustomerQuery query) {
        log.debug("Fetching tasks for customer: {}", query.customerCompanyId());

        return taskRepository.findByCustomer(query.customerCompanyId(), query.pageable());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getTasksByOpportunity(GetTasksByOpportunityQuery query) {
        log.debug("Fetching tasks for opportunity: {}", query.opportunityId());

        return taskRepository.findByOpportunity(query.opportunityId(), query.pageable());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getTasksByAssignee(GetTasksByAssigneeQuery query) {
        log.debug("Fetching tasks for assignee: {}", query.assigneeId());

        return taskRepository.findByAssignee(query.assigneeId(), query.pageable());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getTasksByPriority(GetTasksByPriorityQuery query) {
        log.debug("Fetching tasks by priorities: {}", query.priorities());

        return taskRepository.findByPriorities(query.priorities(), query.pageable());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getOverdueTasks(GetOverdueTasksQuery query) {
        log.debug("Fetching overdue tasks");

        return taskRepository.findOverdueTasks(query.pageable());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getPendingTasks(GetPendingTasksQuery query) {
        log.debug("Fetching pending tasks");

        return taskRepository.findPendingTasks(query.pageable());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> searchTasks(SearchTasksQuery query) {
        log.debug("Searching tasks with criteria: {}", query);

        return taskRepository.searchTasks(
                query.searchTerm(),
                query.statuses(),
                query.priorities(),
                query.customerId(),
                query.assigneeId(),
                query.overdueOnly(),
                query.pageable());
    }

}
