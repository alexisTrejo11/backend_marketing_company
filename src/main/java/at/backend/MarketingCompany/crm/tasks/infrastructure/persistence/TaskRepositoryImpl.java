package at.backend.MarketingCompany.crm.tasks.infrastructure.persistence;

import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import at.backend.MarketingCompany.crm.shared.enums.TaskStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.tasks.domain.entity.Task;
import at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject.TaskId;
import at.backend.MarketingCompany.crm.tasks.domain.repository.TaskRepository;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

  private final JpaTaskRepository jpaTaskRepository;
  private final TaskEntityMapper taskEntityMapper;

  @Override
  @Transactional
  public Task save(Task task) {
    log.debug("Saving task with ID: {}", task.getId().value());

    TaskEntity entity = taskEntityMapper.toEntity(task);
    TaskEntity savedEntity = jpaTaskRepository.save(entity);

    log.info("Task saved successfully with ID: {}", savedEntity.getId());
    return taskEntityMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Task> findById(TaskId taskId) {
    log.debug("Finding task by ID: {}", taskId.value());

    return jpaTaskRepository.findById(taskId.value())
        .map(taskEntityMapper::toDomain);
  }

  @Override
  @Transactional
  public void delete(Task task) {
    log.debug("Deleting task with ID: {}", task.getId().value());

    TaskEntity entity = taskEntityMapper.toEntity(task);
    jpaTaskRepository.delete(entity);

    log.info("Task deleted successfully with ID: {}", task.getId().value());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(TaskId taskId) {
    return jpaTaskRepository.existsById(taskId.value());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Task> findByCustomer(CustomerCompanyId customerCompanyId, Pageable pageable) {
    log.debug("Finding tasks by customer ID: {}", customerCompanyId.value());

    return jpaTaskRepository.findByCustomer_Id(customerCompanyId.value(), pageable)
        .map(taskEntityMapper::toDomain);
  }

  @Override
  public List<Task> findByCustomer(CustomerCompanyId customerCompanyId) {
    return List.of();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Task> findByOpportunity(OpportunityId opportunityId, Pageable pageable) {
    log.debug("Finding tasks by opportunity ID: {}", opportunityId.value());

    return jpaTaskRepository.findByOpportunity_Id(opportunityId.value(), pageable)
        .map(taskEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Task> findByAssignee(EmployeeId assigneeId, Pageable pageable) {
    log.debug("Finding tasks by assignee ID: {}", assigneeId.value());

    return jpaTaskRepository.findByAssignedTo_Id(assigneeId.value(), pageable)
        .map(taskEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Task> findByStatuses(Set<TaskStatus> statuses, Pageable pageable) {
    log.debug("Finding tasks by statuses: {}", statuses);

    return jpaTaskRepository.findByStatusIn(statuses, pageable)
        .map(taskEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Task> findByPriorities(Set<TaskPriority> priorities, Pageable pageable) {
    log.debug("Finding tasks by priorities: {}", priorities);

    return jpaTaskRepository.findByPriorityIn(priorities, pageable)
        .map(taskEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Task> findOverdueTasks(Pageable pageable) {
    log.debug("Finding overdue tasks");

    return jpaTaskRepository.findOverdueTasks(pageable)
        .map(taskEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Task> findPendingTasks(Pageable pageable) {
    log.debug("Finding pending tasks");

    return jpaTaskRepository.findByStatusIn(Set.of(TaskStatus.PENDING, TaskStatus.IN_PROGRESS), pageable)
        .map(taskEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Task> searchTasks(String searchTerm, Set<TaskStatus> statuses,
      Set<TaskPriority> priorities, String customerId,
      String assigneeId, Boolean overdueOnly, Pageable pageable) {
    log.debug("Searching tasks with criteria - term: {}, statuses: {}, priorities: {}",
        searchTerm, statuses, priorities);
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public long countByCustomerAndStatus(CustomerCompanyId customerCompanyId, TaskStatus status) {
    log.debug("Counting tasks for customer {} with status: {}", customerCompanyId.value(), status);

    return jpaTaskRepository.countByCustomer_IdAndStatus(customerCompanyId.value(), status);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByAssigneeAndStatus(EmployeeId assigneeId, TaskStatus status) {
    log.debug("Counting tasks for assignee {} with status: {}", assigneeId.value(), status);

    return jpaTaskRepository.countByAssignedTo_IdAndStatus(assigneeId.value(), status);
  }

  @Override
  @Transactional(readOnly = true)
  public long countOverdueByAssignee(EmployeeId assigneeId) {
    log.debug("Counting overdue tasks for assignee: {}", assigneeId.value());

    return jpaTaskRepository.countOverdueTasksByAssignee(assigneeId.value());
  }
}
