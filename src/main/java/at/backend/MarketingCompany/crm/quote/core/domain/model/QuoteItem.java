package at.backend.MarketingCompany.crm.quote.core.domain.model;

import java.util.Objects;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemReconstructParams;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
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
    this.discountPercentage = Discount.ZERO;
    this.discountAmount = Amount.ZERO;
    this.total = Amount.ZERO;
  }

  public static QuoteItem create(
      QuoteId quoteId,
      ServicePackageId servicePackageId,
      Amount unitPrice,
      Discount discountPercentage) {
    Objects.requireNonNull(quoteId, "Quote ID cannot be null");
    Objects.requireNonNull(servicePackageId, "Service package ID cannot be null");
    Objects.requireNonNull(unitPrice, "Unit price cannot be null");

    QuoteItem item = new QuoteItem();
    item.quoteId = quoteId;
    item.servicePackageId = servicePackageId;
    item.unitPrice = unitPrice;
    item.discountPercentage = discountPercentage != null ? discountPercentage : Discount.ZERO;

    item.calculateTotals();

    return item;
  }

  public static QuoteItem reconstruct(QuoteItemReconstructParams params) {
    QuoteItem item = new QuoteItem();
    item.id = params.id();
    item.quoteId = params.quoteId();
    item.servicePackageId = params.servicePackageId();
    item.unitPrice = params.unitPrice() != null ? params.unitPrice() : Amount.ZERO;
    item.discountPercentage = params.discountPercentage() != null ? params.discountPercentage() : Discount.ZERO;
    item.discountAmount = params.discountAmount() != null ? params.discountAmount() : Amount.ZERO;
    item.total = params.total() != null ? params.total() : Amount.ZERO;
    item.createdAt = params.createdAt();
    item.updatedAt = params.updatedAt();
    item.deletedAt = params.deletedAt();
    item.version = params.version();

    // Recalcular si falta algún campo
    if (item.discountAmount == null || item.total == null) {
      item.calculateTotals();
    }

    return item;
  }

  public Amount totalPrice() {
    if (unitPrice == null) {
      return Amount.ZERO;
    }
    if (discountAmount == null) {
      discountAmount = Amount.ZERO;
    }
    return unitPrice.subtract(discountAmount);
  }

  private void calculateTotals() {
    if (unitPrice == null) {
      throw new IllegalStateException("Unit price must be set before calculating totals");
    }

    if (discountPercentage == null) {
      discountPercentage = Discount.ZERO;
    }

    this.discountAmount = Discount.calculateDiscountAmount(unitPrice, discountPercentage.percentage());

    this.total = unitPrice.subtract(discountAmount);
  }

  public void updateUnitPrice(Amount newUnitPrice) {
    this.unitPrice = Objects.requireNonNull(newUnitPrice, "Unit price cannot be null");
    calculateTotals();
  }

  public void updateDiscount(Discount newDiscount) {
    this.discountPercentage = newDiscount != null ? newDiscount : Discount.ZERO;
    calculateTotals();
  }
}
