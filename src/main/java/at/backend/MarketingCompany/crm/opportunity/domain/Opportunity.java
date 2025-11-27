package at.backend.MarketingCompany.crm.opportunity.domain;

import at.backend.MarketingCompany.crm.tasks.domain.Task;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "opportunities")
public class Opportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerModel customerModel;

    @Column(name = "title")
    private String title;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "stage")
    private String stage;  // LEAD, QUALIFIED, PROPOSAL, NEGOTIATION, CLOSED_WON, CLOSED_LOST

    @Column(name = "expected_close_date")
    private LocalDate expectedCloseDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "opportunity")
    private List<Task> tasks;

    public Opportunity(UUID id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
