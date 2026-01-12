package at.backend.MarketingCompany.crm.quote.adapter.output.entity;

import java.time.LocalDate;
import java.util.List;

import at.backend.MarketingCompany.crm.opportunity.adapter.output.persistence.OpportunityEntity;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteStatus;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quotes")
public class QuoteEntity extends BaseJpaEntity {

  @ManyToOne
  @JoinColumn(name = "customer_company_id", nullable = false)
  private CustomerCompanyEntity customerCompany;

  @ManyToOne
  @JoinColumn(name = "opportunity_id", nullable = true)
  private OpportunityEntity opportunity;

  @Column(name = "valid_until", nullable = false)
  private LocalDate validUntil;

  @Column(name = "status", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private QuoteStatus status;

  @OneToMany(mappedBy = "quote", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<QuoteItemEntity> items;

  public QuoteEntity(Long id) {
    this.setId(id);
  }
}
