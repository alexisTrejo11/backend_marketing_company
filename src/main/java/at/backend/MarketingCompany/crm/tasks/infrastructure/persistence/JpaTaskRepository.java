package at.backend.MarketingCompany.crm.tasks.infrastructure.persistence;

import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import at.backend.MarketingCompany.crm.shared.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, String> {

    // Basic finders (use nested property traversal with underscore)
    Page<TaskEntity> findByCustomer_Id(String customerId, Pageable pageable);
    Page<TaskEntity> findByOpportunity_Id(String opportunityId, Pageable pageable);
    Page<TaskEntity> findByAssignedTo_Id(String assignedToId, Pageable pageable);
    Page<TaskEntity> findByStatus(TaskStatus status, Pageable pageable);
    Page<TaskEntity> findByStatusIn(Set<TaskStatus> statuses, Pageable pageable);
    Page<TaskEntity> findByPriority(TaskPriority priority, Pageable pageable);
    Page<TaskEntity> findByPriorityIn(Set<TaskPriority> priorities, Pageable pageable);

    // Search
    @Query("SELECT t FROM TaskEntity t WHERE " +
            "(LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<TaskEntity> searchByTitleOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Overdue tasks (due date is past and status is not completed/cancelled)
    @Query("SELECT t FROM TaskEntity t WHERE " +
            "t.dueDate < :now AND " +
            "t.status NOT IN (:excludedStatuses)")
    Page<TaskEntity> findOverdueTasks(@Param("now") LocalDateTime now,
                                      @Param("excludedStatuses") Set<TaskStatus> excludedStatuses,
                                      Pageable pageable);

    // Default method for overdue tasks
    default Page<TaskEntity> findOverdueTasks(Pageable pageable) {
        return findOverdueTasks(
                LocalDateTime.now(),
                Set.of(TaskStatus.COMPLETED, TaskStatus.CANCELLED),
                pageable
        );
    }

    // Count queries (use nested property)
    long countByCustomer_IdAndStatus(String customerId, TaskStatus status);
    long countByAssignedTo_IdAndStatus(String assignedToId, TaskStatus status);

    // Count overdue tasks by assignee
    @Query("SELECT COUNT(t) FROM TaskEntity t WHERE " +
            "t.assignedTo.id = :assigneeId AND " +
            "t.dueDate < :now AND " +
            "t.status NOT IN (:excludedStatuses)")
    long countOverdueTasksByAssignee(@Param("assigneeId") String assigneeId,
                                     @Param("now") LocalDateTime now,
                                     @Param("excludedStatuses") List<TaskStatus> excludedStatuses);

    // Default method for count overdue by assignee
    default long countOverdueTasksByAssignee(String assigneeId) {
        return countOverdueTasksByAssignee(
                assigneeId,
                LocalDateTime.now(),
                List.of(TaskStatus.COMPLETED, TaskStatus.CANCELLED)
        );
    }

    // Find tasks by multiple criteria (fix property names to use t.customer.id)
    @Query("SELECT t FROM TaskEntity t WHERE " +
            "(:customerCompanyId IS NULL OR t.customer.id = :customerCompanyId) AND " +
            "(:assigneeId IS NULL OR t.assignedTo.id = :assigneeId) AND " +
            "(:statuses IS NULL OR t.status IN :statuses) AND " +
            "(:priorities IS NULL OR t.priority IN :priorities)")
    List<TaskEntity> findByCriteria(@Param("customerCompanyId") String customerId,
                                    @Param("assigneeId") String assigneeId,
                                    @Param("statuses") List<TaskStatus> statuses,
                                    @Param("priorities") List<TaskPriority> priorities);
}
