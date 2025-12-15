package at.backend.MarketingCompany.crm.tasks.domain.entity;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import at.backend.MarketingCompany.crm.shared.enums.TaskStatus;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.tasks.domain.exceptions.TaskValidationException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class Task extends BaseDomainEntity<TaskId> {
  private CustomerCompanyId customerCompanyId;
  private OpportunityId opportunityId;
  private String title;
  private String description;
  private DueDate dueDate;
  private TaskStatus status;
  private TaskPriority priority;
  private EmployeeId assignedTo;

  private Task(TaskId taskId) {
    super(taskId);
  }

  private Task(TaskReconstructParams params) {
    super(params.id(), params.version(), params.deletedAt(), params.createdAt(), params.updatedAt());
    this.customerCompanyId = params.customerCompanyId();
    this.opportunityId = params.opportunityId();
    this.title = params.title();
    this.description = params.description();
    this.dueDate = params.dueDate();
    this.status = params.status();
    this.priority = params.priority();
    this.assignedTo = params.assignedTo();
    validateState();
  }

  public static Task reconstruct(TaskReconstructParams params) {
    return new Task(params);
  }

  public static Task create(CreateTaskParams params) {
    validateCreationParams(params);

    Task newTask = new Task(TaskId.create());
    newTask.customerCompanyId = params.customerCompanyId();
    newTask.opportunityId = params.opportunityId();
    newTask.title = params.title();
    newTask.description = params.description();
    newTask.dueDate = params.dueDate();
    newTask.status = TaskStatus.PENDING;
    newTask.priority = params.priority();
    newTask.assignedTo = params.assignedTo();
    newTask.createdAt = LocalDateTime.now();
    newTask.updatedAt = LocalDateTime.now();
    newTask.version = 1;

    return newTask;
  }

  public void updateDetails(String title, String description, TaskPriority priority) {
    if (title == null || title.trim().isEmpty()) {
      throw new TaskValidationException("Task title cannot be empty");
    }
    if (priority == null) {
      throw new TaskValidationException("Task priority is required");
    }

    this.title = title.trim();
    this.description = description != null ? description.trim() : null;
    this.priority = priority;
    updateTimestamp();
  }

  public void assignTo(EmployeeId employeeId) {
    this.assignedTo = employeeId;
    updateTimestamp();
  }

  public void unassign() {
    this.assignedTo = null;
    updateTimestamp();
  }

  public void changeDueDate(DueDate newDueDate) {
    if (newDueDate != null && newDueDate.isPast()) {
      throw new TaskValidationException("Due date cannot be in the past");
    }
    this.dueDate = newDueDate;
    updateTimestamp();
  }

  public void markInProgress() {
    if (this.status != TaskStatus.PENDING) {
      throw new TaskValidationException("Only pending tasks can be marked as in progress");
    }
    this.status = TaskStatus.IN_PROGRESS;
    updateTimestamp();
  }

  public void complete() {
    if (this.status == TaskStatus.COMPLETED) {
      throw new TaskValidationException("Task is already completed");
    }
    this.status = TaskStatus.COMPLETED;
    updateTimestamp();
  }

  public void cancel() {
    if (this.status == TaskStatus.CANCELLED) {
      throw new TaskValidationException("Task is already cancelled");
    }
    this.status = TaskStatus.CANCELLED;
    updateTimestamp();
  }

  public void reopen() {
    if (this.status != TaskStatus.COMPLETED && this.status != TaskStatus.CANCELLED) {
      throw new TaskValidationException("Only completed or cancelled tasks can be reopened");
    }
    this.status = TaskStatus.PENDING;
    updateTimestamp();
  }

  public boolean isOverdue() {
    return dueDate != null && dueDate.isOverdue();
  }

  public boolean isAssigned() {
    return assignedTo != null;
  }

  public boolean canBeModified() {
    return status != TaskStatus.COMPLETED && status != TaskStatus.CANCELLED;
  }

  private void validateState() {
    if (customerCompanyId == null) {
      throw new TaskValidationException("Customer ID is required");
    }
    if (title == null || title.trim().isEmpty()) {
      throw new TaskValidationException("Task title is required");
    }
    if (status == null) {
      throw new TaskValidationException("Task status is required");
    }
    if (priority == null) {
      throw new TaskValidationException("Task priority is required");
    }
  }

  private static void validateCreationParams(CreateTaskParams params) {
    if (params == null) {
      throw new TaskValidationException("Creation parameters cannot be null");
    }
    if (params.customerCompanyId() == null) {
      throw new TaskValidationException("Customer ID is required");
    }
    if (params.title() == null || params.title().trim().isEmpty()) {
      throw new TaskValidationException("Task title is required");
    }
    if (params.priority() == null) {
      throw new TaskValidationException("Task priority is required");
    }
  }

  private void updateTimestamp() {
    this.updatedAt = LocalDateTime.now();
  }

  public Optional<DueDate> getDueDate() {
    return Optional.ofNullable(dueDate);
  }

  public Optional<EmployeeId> getAssignedTo() {
    return Optional.ofNullable(assignedTo);
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  @Override
  public void softDelete() {
    if (status != TaskStatus.COMPLETED && status != TaskStatus.CANCELLED) {
      throw new TaskValidationException("Only completed or cancelled tasks can be deleted");
    }
    super.softDelete();
  }
}
