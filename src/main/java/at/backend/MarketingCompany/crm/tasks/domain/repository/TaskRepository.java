package at.backend.MarketingCompany.crm.tasks.domain.repository;

import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import at.backend.MarketingCompany.crm.shared.enums.TaskStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.tasks.domain.entity.Task;
import at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject.TaskId;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskRepository {
  Task save(Task task);

  Optional<Task> findById(TaskId taskId);

  void delete(Task task);

  boolean existsById(TaskId taskId);

  Page<Task> findByCustomer(CustomerCompanyId customerCompanyId, Pageable pageable);

  List<Task> findByCustomer(CustomerCompanyId customerCompanyId);

  Page<Task> findByOpportunity(OpportunityId opportunityId, Pageable pageable);

  Page<Task> findByAssignee(EmployeeId assigneeId, Pageable pageable);

  Page<Task> findByStatuses(Set<TaskStatus> statuses, Pageable pageable);

  Page<Task> findByPriorities(Set<TaskPriority> priorities, Pageable pageable);

  Page<Task> findOverdueTasks(Pageable pageable);

  Page<Task> findPendingTasks(Pageable pageable);

  Page<Task> searchTasks(String searchTerm, Set<TaskStatus> statuses,
      Set<TaskPriority> priorities, String customerId,
      String assigneeId, Boolean overdueOnly, Pageable pageable);

  long countByCustomerAndStatus(CustomerCompanyId customerCompanyId, TaskStatus status);

  long countByAssigneeAndStatus(EmployeeId assigneeId, TaskStatus status);

  long countOverdueByAssignee(EmployeeId assigneeId);
}
