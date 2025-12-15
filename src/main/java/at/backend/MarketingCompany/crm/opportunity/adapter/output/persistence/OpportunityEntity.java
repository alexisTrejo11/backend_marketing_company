package at.backend.MarketingCompany.crm.opportunity.adapter.output.persistence;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "opportunities")
public class OpportunityEntity extends BaseJpaEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_company_id", nullable = false)
  private CustomerCompanyEntity customerCompany;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "amount", precision = 15, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "stage", nullable = false)
  private OpportunityStage stage;

  @Column(name = "expected_close_date")
  private LocalDate expectedCloseDate;

  public OpportunityEntity(String id) {
    this.setId(id);
  }
}
