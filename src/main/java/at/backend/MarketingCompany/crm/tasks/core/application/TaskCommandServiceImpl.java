package at.backend.MarketingCompany.crm.tasks.core.application;

import at.backend.MarketingCompany.crm.deal.core.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.tasks.core.application.commands.*;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.Task;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.CreateTaskParams;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;
import at.backend.MarketingCompany.crm.tasks.core.domain.exceptions.TaskNotFoundException;
import at.backend.MarketingCompany.crm.tasks.core.port.input.TaskCommandService;
import at.backend.MarketingCompany.shared.domain.exceptions.ExternalServiceException;
import at.backend.MarketingCompany.crm.tasks.core.port.output.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskCommandServiceImpl implements TaskCommandService {

  private final TaskRepository taskRepository;
  private final ExternalModuleValidator externalValidator;

  @Override
  @Transactional
  public Task createTask(CreateTaskCommand command) {
    log.info("Creating task for customer: {}", command.customerCompanyId());

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

    log.info("Task created successfully with ID: {}", savedTask.getId());
    return savedTask;
  }

  @Override
  @Transactional
  public Task updateTask(UpdateTaskDetailsCommand command) {
    log.info("Updating task details: {}", command.taskId());

    Task task = findTaskOrThrow(command.taskId());

    if (!task.canBeModified()) {
      throw new IllegalStateException("Cannot modify a completed or cancelled task");
    }

    task.updateDetails(command.title(), command.description(), command.priority());

    Task updatedTask = taskRepository.save(task);
    log.info("Task {} details updated successfully", command.taskId());

    return updatedTask;
  }

  @Override
  @Transactional
  public Task assingTask(AssignTaskCommand command) {
    log.info("Assigning task {} to employee: {}", command.taskId(), command.assignedTo().value());

    Task task = findTaskOrThrow(command.taskId());

    if (!task.canBeModified()) {
      throw new IllegalStateException("Cannot assign a completed or cancelled task");
    }

    externalValidator.validateEmployeeExists(command.assignedTo());
    task.assignTo(command.assignedTo());

    Task updatedTask = taskRepository.save(task);
    log.info("Task {} assigned successfully to {}", command.taskId(), command.assignedTo().value());

    return updatedTask;
  }

  @Override
  @Transactional
  public Task unassignTask(UnassignTaskCommand command) {
    log.info("Unassigning task: {}", command.taskId());

    Task task = findTaskOrThrow(command.taskId());

    if (!task.canBeModified()) {
      throw new IllegalStateException("Cannot unassign a completed or cancelled task");
    }

    task.unassign();
    Task updatedTask = taskRepository.save(task);
    log.info("Task {} unassigned successfully", command.taskId());

    return updatedTask;
  }

  @Override
  @Transactional
  public Task changeTaskDueDate(ChangeTaskDueDateCommand command) {
    log.info("Changing due date for task: {}", command.taskId());

    Task task = findTaskOrThrow(command.taskId());

    if (!task.canBeModified()) {
      throw new IllegalStateException("Cannot change due date of a completed or cancelled task");
    }

    task.changeDueDate(command.dueDate());
    Task updatedTask = taskRepository.save(task);
    log.info("Due date updated for task {}", command.taskId());

    return updatedTask;
  }

  @Override
  @Transactional
  public Task markTaskInProgress(MarkTaskInProgressCommand command) {
    return changeTaskStatus(command.taskId(), "in progress", Task::markInProgress);
  }

  @Override
  @Transactional
  public Task completeTask(CompleteTaskCommand command) {
    return changeTaskStatus(command.taskId(), "completed", Task::complete);
  }

  @Override
  @Transactional
  public Task cancelTask(CancelTaskCommand command) {
    return changeTaskStatus(command.taskId(), "cancelled", Task::cancel);
  }

  @Override
  @Transactional
  public Task reopenTask(ReopenTaskCommand command) {
    return changeTaskStatus(command.taskId(), "reopened", Task::reopen);
  }

  @Override
  @Transactional
  public void deleteTask(DeleteTaskCommand command) {
    log.info("Deleting task: {}", command.taskId());

    Task task = findTaskOrThrow(command.taskId());
    task.softDelete();

    taskRepository.delete(task);
    log.info("Task {} deleted successfully", command.taskId());
  }

  private Task findTaskOrThrow(TaskId taskId) {
    return taskRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException(taskId));
  }

  private Task changeTaskStatus(TaskId taskId, String statusName, TaskStatusChanger statusChanger) {
    log.info("Marking task {} as {}", taskId, statusName);

    Task task = findTaskOrThrow(taskId);
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

  @FunctionalInterface
  private interface TaskStatusChanger {
    void changeStatus(Task task);
  }
}
