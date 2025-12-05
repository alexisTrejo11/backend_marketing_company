package at.backend.MarketingCompany.crm.quote.domain.model;


import at.backend.MarketingCompany.common.utils.BaseDomainEntity;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import lombok.Getter;

@Getter
public class QuoteItem extends BaseDomainEntity<QuoteItemId> {
    private QuoteId quoteId;
    private ServicePackageId servicePackageId;
    private Amount unitPrice;
    private Amount total;
    private Discount discountPercentage;
    private Amount discountAmount;

    private QuoteItem() {}
    
    public static QuoteItem create(
        QuoteId quoteId,
        ServicePackageId servicePackageId,
        Amount unitPrice,
        Discount discountPercentage
    ) {
        QuoteItem item = new QuoteItem();
        item.quoteId = quoteId;
        item.servicePackageId = servicePackageId;
        item.unitPrice = unitPrice;
        item.discountPercentage = discountPercentage;
        item.calculateTotals();
        return item;
    }
    
    private void calculateTotals() {
        this.discountAmount = discountPercentage.calculateDiscountAmount(unitPrice);
        this.total = unitPrice.subtract(discountAmount);
    }
}