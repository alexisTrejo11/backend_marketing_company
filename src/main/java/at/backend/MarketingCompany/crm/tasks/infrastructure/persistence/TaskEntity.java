package at.backend.MarketingCompany.crm.tasks.infrastructure.persistence;

import at.backend.MarketingCompany.common.jpa.BaseJpaEntity;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.persistence.OpportunityEntity;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.user.api.Model.User;
import at.backend.MarketingCompany.crm.shared.enums.TaskPriority;
import at.backend.MarketingCompany.crm.shared.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
@Getter
@SuperBuilder
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity extends BaseJpaEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerModel customerModel;

    @ManyToOne
    @JoinColumn(name = "opportunity_id")
    private OpportunityEntity opportunity;

    @ManyToOne
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedTo;
}
