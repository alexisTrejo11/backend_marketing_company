package at.backend.MarketingCompany.crm.quote.domain.model;

import at.backend.MarketingCompany.common.utils.BaseDomainEntity;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteStatus;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Quote extends BaseDomainEntity<QuoteId> {
    private CustomerId customerId;
    private OpportunityId opportunityId;
    private LocalDate validUntil;
    private Amount subTotal;
    private Discount discount;
    private Amount totalAmount;
    private QuoteStatus status;
    private List<QuoteItem> items;

    private Quote() {
        this.items = new ArrayList<>();
        this.subTotal = Amount.zero();
        this.discount = Discount.ZERO;
        this.totalAmount = Amount.zero();
        this.status = QuoteStatus.DRAFT;
    }
    
    public static Quote create(CustomerId customerId, LocalDate validUntil) {
        Quote quote = new Quote();
        quote.customerId = customerId;
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