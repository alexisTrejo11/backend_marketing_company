package at.backend.MarketingCompany.crm.quote.adapter.output.entity;

import java.math.BigDecimal;

import at.backend.MarketingCompany.crm.servicePackage.adapter.output.model.ServicePackageEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quote_items")
public class QuoteItemEntity extends BaseJpaEntity {
  @ManyToOne
  @JoinColumn(name = "quote_id", nullable = false)
  private QuoteEntity quote;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_package_id", nullable = false)
  private ServicePackageEntity servicePackage;

  @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal unitPrice;

  @Column(name = "total", nullable = false, precision = 10, scale = 2)
  private BigDecimal total;

  @Column(name = "discount_percentage", precision = 5, scale = 2)
  private BigDecimal discountPercentage;

  @Column(name = "discount", precision = 5, scale = 2)
  private BigDecimal discount;

  public QuoteItemEntity(BigDecimal discountPercentage, QuoteEntity quoteEntity) {
    this.discountPercentage = discountPercentage;
    this.quote = quoteEntity;
  }

  public QuoteItemEntity(Long id) {
    this.setId(id);
  }
}
