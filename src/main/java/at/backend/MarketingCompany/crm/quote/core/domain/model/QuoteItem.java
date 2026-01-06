package at.backend.MarketingCompany.crm.quote.core.domain.model;

import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemReconstructParams;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import lombok.Getter;

@Getter
public class QuoteItem extends BaseDomainEntity<QuoteItemId> {
  private QuoteId quoteId;
  private ServicePackageId servicePackageId;
  private Amount unitPrice;
  private Amount total;
  private Discount discountPercentage;
  private Amount discountAmount;

  private QuoteItem() {
  }

  public static QuoteItem create(
      QuoteId quoteId,
      ServicePackageId servicePackageId,
      Amount unitPrice,
      Discount discountPercentage) {
    QuoteItem item = new QuoteItem();
    item.quoteId = quoteId;
    item.servicePackageId = servicePackageId;
    item.unitPrice = unitPrice;
    item.discountPercentage = discountPercentage;

    item.calculateTotals();

    return item;
  }

  public static QuoteItem reconstruct(QuoteItemReconstructParams params) {
    QuoteItem item = new QuoteItem();
    item.id = params.id();
    item.quoteId = params.quoteId();
    item.servicePackageId = params.servicePackageId();
    item.unitPrice = params.unitPrice();
    item.discountPercentage = params.discountPercentage();
    item.discountAmount = params.discountAmount();
    item.total = params.total();
    item.createdAt = params.createdAt();
    item.updatedAt = params.updatedAt();
    item.deletedAt = params.deletedAt();
    item.version = params.version();

    item.calculateTotals();

    return item;
  }

  public Amount totalPrice() {
    return unitPrice.subtract(discountAmount);
  }

  private void calculateTotals() {
    this.discountAmount = discountPercentage.calculateDiscountAmount(unitPrice);
    this.total = unitPrice.subtract(discountAmount);
  }
}
