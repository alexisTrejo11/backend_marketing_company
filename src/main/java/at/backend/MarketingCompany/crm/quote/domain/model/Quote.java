package at.backend.MarketingCompany.crm.quote.domain.model;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Discount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteReconstructParams;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Quote extends BaseDomainEntity<QuoteId> {
  private CustomerCompanyId customerCompanyId;
  private OpportunityId opportunityId;
  private LocalDate validUntil;
  private Amount subTotal;
  private Discount discount;
  private Amount totalAmount;
  private QuoteStatus status;
  private final List<QuoteItem> items;

  private Quote() {
    this.items = new ArrayList<>();
    this.subTotal = Amount.ZERO;
    this.discount = Discount.ZERO;
    this.totalAmount = Amount.ZERO;
    this.status = QuoteStatus.DRAFT;
  }

  public static Quote reconstruct(QuoteReconstructParams params) {
    Quote quote = new Quote();
    quote.id = params.id();
    quote.customerCompanyId = params.customerCompanyId();
    quote.opportunityId = params.opportunityId();
    quote.validUntil = params.validUntil();
    quote.subTotal = params.subTotal();
    quote.discount = params.discount();
    quote.totalAmount = params.totalAmount();
    quote.status = params.status();
    // Note: Items reconstruction would require fetching QuoteItem entities by their
    // IDs
    quote.createdAt = params.createdAt();
    quote.updatedAt = params.updatedAt();
    quote.deletedAt = params.deletedAt();
    quote.version = params.version();

    return quote;
  }

  public static Quote create(CustomerCompanyId customerCompanyId, LocalDate validUntil) {
    Quote quote = new Quote();
    quote.customerCompanyId = customerCompanyId;
    quote.validUntil = validUntil;
    return quote;
  }

  public void addItem(QuoteItem item) {
    if (item == null) {
      throw new IllegalArgumentException("Item cannot be null");
    }
    this.items.add(item);
    recalculateTotals();
    this.updatedAt = LocalDateTime.now();
  }

  public void addItems(List<QuoteItem> items) {
    if (items == null || items.isEmpty()) {
      throw new IllegalArgumentException("Items cannot be null or empty");
    }
    this.items.addAll(items);
    recalculateTotals();
    this.updatedAt = LocalDateTime.now();
  }

  public void removeItem(QuoteItemId itemId) {
    this.items.removeIf(item -> item.getId().equals(itemId));
    recalculateTotals();
    this.updatedAt = LocalDateTime.now();
  }

  private void recalculateTotals() {
    Amount newSubTotal = Amount.ZERO;
    Amount newTotal = Amount.ZERO;

    for (QuoteItem item : items) {
      newSubTotal = newSubTotal.add(item.getUnitPrice());
      newTotal = newTotal.add(item.getTotal());
    }

    this.subTotal = newSubTotal;
    this.totalAmount = newTotal;
  }

  public void setOpportunity(OpportunityId opportunityId) {
    this.opportunityId = opportunityId;
    this.updatedAt = LocalDateTime.now();
  }

  public void markAsSent() {
    this.status = QuoteStatus.SENT;
    this.updatedAt = LocalDateTime.now();
  }

  public void validate() {
    if (validUntil.isBefore(LocalDate.now())) {
      throw new IllegalStateException("Quote has expired");
    }
    if (items.isEmpty()) {
      throw new IllegalStateException("Quote must have at least one item");
    }
  }

}
