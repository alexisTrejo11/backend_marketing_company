package at.backend.MarketingCompany.crm.tasks.infrastructure.persistence;

import at.backend.MarketingCompany.account.user.domain.repository.UserEntity;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.persistence.OpportunityEntity;
import at.backend.MarketingCompany.crm.tasks.domain.entity.Task;
import at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject.*;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class TaskEntityMapper {

  public TaskEntity toEntity(Task task) {
    if (task == null)
      return null;

    TaskEntity entity = new TaskEntity();

    if (task.getId() != null) {
      entity.setId(task.getId().value());
    }

    entity.setTitle(task.getTitle());
    entity.setStatus(task.getStatus());
    entity.setPriority(task.getPriority());

    task.getDescription().ifPresent(entity::setDescription);
    task.getDueDate().ifPresent(dueDate -> entity.setDueDate(dueDate.value()));

    entity.setCreatedAt(task.getCreatedAt());
    entity.setUpdatedAt(task.getUpdatedAt());
    entity.setDeletedAt(task.getDeletedAt());
    entity.setVersion(task.getVersion());

    if (task.getCustomerId() != null) {
      var customer = new CustomerModel(task.getCustomerId().value());
      entity.setCustomerModel(customer);
    }

    if (task.getOpportunityId() != null) {
      var opportunity = new OpportunityEntity(task.getOpportunityId().value());
      entity.setOpportunity(opportunity);
    }

    task.getAssignedTo().ifPresent(assignedTo -> {
      var user = new UserEntity(assignedTo.value());
      entity.setAssignedTo(user);
    });

    return entity;
  }

  public Task toDomain(TaskEntity entity) {
    if (entity == null)
      return null;

    var reconstructParams = TaskReconstructParams.builder()
        .id(TaskId.from(entity.getId()))
        .customerId(entity.getCustomerModel() != null ? new CustomerId(entity.getCustomerModel().getId()) : null)
        .opportunityId(entity.getOpportunity() != null ? new OpportunityId(entity.getOpportunity().getId()) : null)
        .title(entity.getTitle())
        .description(entity.getDescription())
        .dueDate(entity.getDueDate() != null ? DueDate.from(entity.getDueDate()) : null)
        .status(entity.getStatus())
        .priority(entity.getPriority())
        .assignedTo(entity.getAssignedTo() != null ? new EmployeeId(entity.getAssignedTo().getId()) : null)
        .version(entity.getVersion())
        .deletedAt(entity.getDeletedAt())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();

    return Task.reconstruct(reconstructParams);
  }

  public void updateEntity(TaskEntity existingEntity, Task task) {
    existingEntity.setTitle(task.getTitle());
    existingEntity.setStatus(task.getStatus());
    existingEntity.setPriority(task.getPriority());

    task.getDescription().ifPresentOrElse(
        existingEntity::setDescription,
        () -> existingEntity.setDescription(null));

    task.getDueDate().ifPresentOrElse(
        dueDate -> existingEntity.setDueDate(dueDate.value()),
        () -> existingEntity.setDueDate(null));
  }
}
