package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.entity;

import at.backend.MarketingCompany.common.jpa.BaseJpaEntity;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteStatus;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.persistence.OpportunityEntity;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quotes")
public class QuoteEntity extends BaseJpaEntity {

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private CustomerModel customer;

  @ManyToOne
  @JoinColumn(name = "opportunity_id", nullable = true)
  private OpportunityEntity opportunity;

  @Column(name = "valid_until", nullable = false)
  private LocalDate validUntil;

  @Column(name = "sub_total", nullable = false, precision = 10, scale = 2)
  private BigDecimal subTotal;

  @Column(name = "discount", precision = 5, scale = 2)
  private BigDecimal discount;

  @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalAmount;

  @Column(name = "status", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private QuoteStatus status;

  @OneToMany(mappedBy = "quote", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<QuoteItemEntity> items;
}
