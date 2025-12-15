package at.backend.MarketingCompany.crm.tasks.application;

import at.backend.MarketingCompany.crm.deal.core.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.shared.enums.TaskStatus;
import at.backend.MarketingCompany.crm.tasks.application.commands.*;
import at.backend.MarketingCompany.crm.tasks.application.queries.*;
import at.backend.MarketingCompany.crm.tasks.domain.entity.Task;
import at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.tasks.domain.exceptions.TaskNotFoundException;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.domain.exceptions.ExternalServiceException;
import at.backend.MarketingCompany.crm.tasks.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskApplicationService {

  private final TaskRepository taskRepository;
  private final ExternalModuleValidator externalValidator;

  @Transactional
  public Task handle(CreateTaskCommand command) {
    log.info("Creating task for customer: {}", command.customerCompanyId().value());

    validateExternalDependencies(command);

    var createParams = CreateTaskParams.builder()
        .customerCompanyId(command.customerCompanyId())
        .opportunityId(command.opportunityId())
        .title(command.title())
        .description(command.description())
        .dueDate(command.dueDate())
        .priority(command.priority())
        .assignedTo(command.assignedTo())
        .build();

    Task newTask = Task.create(createParams);
    Task savedTask = taskRepository.save(newTask);

    log.info("Task created successfully with ID: {}", savedTask.getId().value());
    return savedTask;
  }

  @Transactional
  public Task handle(UpdateTaskDetailsCommand command) {
    log.info("Updating task details: {}", command.taskId());

    Task task = findTaskOrThrow(TaskId.from(command.taskId()));

    if (!task.canBeModified()) {
      throw new IllegalStateException("Cannot modify a completed or cancelled task");
    }

    task.updateDetails(command.title(), command.description(), command.priority());

    Task updatedTask = taskRepository.save(task);
    log.info("Task {} details updated successfully", command.taskId());

    return updatedTask;
  }

  @Transactional
  public Task handle(AssignTaskCommand command) {
    log.info("Assigning task {} to employee: {}", command.taskId(), command.assignedTo().value());

    Task task = findTaskOrThrow(TaskId.from(command.taskId()));

    if (!task.canBeModified()) {
      throw new IllegalStateException("Cannot assign a completed or cancelled task");
    }

    externalValidator.validateEmployeeExists(command.assignedTo());
    task.assignTo(command.assignedTo());

    Task updatedTask = taskRepository.save(task);
    log.info("Task {} assigned successfully to {}", command.taskId(), command.assignedTo().value());

    return updatedTask;
  }

  @Transactional
  public Task handle(UnassignTaskCommand command) {
    log.info("Unassigning task: {}", command.taskId());

    Task task = findTaskOrThrow(TaskId.from(command.taskId()));

    if (!task.canBeModified()) {
      throw new IllegalStateException("Cannot unassign a completed or cancelled task");
    }

    task.unassign();
    Task updatedTask = taskRepository.save(task);
    log.info("Task {} unassigned successfully", command.taskId());

    return updatedTask;
  }

  @Transactional
  public Task handle(ChangeTaskDueDateCommand command) {
    log.info("Changing due date for task: {}", command.taskId());

    Task task = findTaskOrThrow(TaskId.from(command.taskId()));

    if (!task.canBeModified()) {
      throw new IllegalStateException("Cannot change due date of a completed or cancelled task");
    }

    task.changeDueDate(command.dueDate());
    Task updatedTask = taskRepository.save(task);
    log.info("Due date updated for task {}", command.taskId());

    return updatedTask;
  }

  @Transactional
  public Task handle(MarkTaskInProgressCommand command) {
    return changeTaskStatus(command.taskId(), "in progress", Task::markInProgress);
  }

  @Transactional
  public Task handle(CompleteTaskCommand command) {
    return changeTaskStatus(command.taskId(), "completed", Task::complete);
  }

  @Transactional
  public Task handle(CancelTaskCommand command) {
    return changeTaskStatus(command.taskId(), "cancelled", Task::cancel);
  }

  @Transactional
  public Task handle(ReopenTaskCommand command) {
    return changeTaskStatus(command.taskId(), "reopened", Task::reopen);
  }

  @Transactional
  public void handle(DeleteTaskCommand command) {
    log.info("Deleting task: {}", command.taskId());

    Task task = findTaskOrThrow(TaskId.from(command.taskId()));
    task.softDelete();

    taskRepository.delete(task);
    log.info("Task {} deleted successfully", command.taskId());
  }

  // ===== QUERY HANDLERS =====

  @Transactional(readOnly = true)
  public Task handle(GetTaskByIdQuery query) {
    log.debug("Fetching task by ID: {}", query.taskId());

    return taskRepository.findById(TaskId.from(query.taskId()))
        .orElseThrow(() -> new TaskNotFoundException(query.taskId()));
  }

  @Transactional(readOnly = true)
  public Page<Task> handle(GetTasksByStatusQuery query) {
    log.debug("Fetching tasks by status: {}", query.statuses());

    return taskRepository.findByStatuses(query.statuses(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Task> handle(GetTasksByCustomerQuery query) {
    log.debug("Fetching tasks for customer: {}", query.customerCompanyId());

    return taskRepository.findByCustomer(query.customerCompanyId(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Task> handle(GetTasksByOpportunityQuery query) {
    log.debug("Fetching tasks for opportunity: {}", query.opportunityId());

    return taskRepository.findByOpportunity(query.opportunityId(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Task> handle(GetTasksByAssigneeQuery query) {
    log.debug("Fetching tasks for assignee: {}", query.assigneeId());

    return taskRepository.findByAssignee(query.assigneeId(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Task> handle(GetTasksByPriorityQuery query) {
    log.debug("Fetching tasks by priorities: {}", query.priorities());

    return taskRepository.findByPriorities(query.priorities(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Task> handle(GetOverdueTasksQuery query) {
    log.debug("Fetching overdue tasks");

    return taskRepository.findOverdueTasks(query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Task> handle(GetPendingTasksQuery query) {
    log.debug("Fetching pending tasks");

    return taskRepository.findPendingTasks(query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Task> handle(SearchTasksQuery query) {
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

  @Transactional(readOnly = true)
  public TaskStatistics handle(GetTaskStatisticsQuery query) {
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

  // ===== PRIVATE METHODS =====

  private Task findTaskOrThrow(TaskId taskId) {
    return taskRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException(taskId.value()));
  }

  private Task changeTaskStatus(String taskId, String statusName, TaskStatusChanger statusChanger) {
    log.info("Marking task {} as {}", taskId, statusName);

    Task task = findTaskOrThrow(TaskId.from(taskId));
    statusChanger.changeStatus(task);

    Task updatedTask = taskRepository.save(task);
    log.info("Task {} marked as {}", taskId, statusName);

    return updatedTask;
  }

  private void validateExternalDependencies(CreateTaskCommand command) {
    try {
      externalValidator.validateCustomerExists(command.customerCompanyId());

      if (command.opportunityId() != null) {
        externalValidator.validateOpportunityExists(command.opportunityId());
      }

      if (command.assignedTo() != null) {
        externalValidator.validateEmployeeExists(command.assignedTo());
      }
    } catch (ExternalServiceException e) {
      log.error("External validation failed for task creation: {}", e.getMessage());
      throw e;
    }
  }

  private long countTasksByCustomer(CustomerCompanyId customerCompanyId) {
    return taskRepository.findByCustomer(customerCompanyId).size();
  }

  @FunctionalInterface
  private interface TaskStatusChanger {
    void changeStatus(Task task);
  }

  public record TaskStatistics(
      long totalTasks,
      long pendingTasks,
      long completedTasks,
      long overdueTasks) {
  }
}
