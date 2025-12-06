package at.backend.MarketingCompany.crm.quote.domain.valueobject;

import java.time.LocalDateTime;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Discount;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;

public record QuoteItemReconstructParams(
    QuoteItemId id,
    QuoteId quoteId,
    ServicePackageId servicePackageId,
    Amount unitPrice,
    Discount discountPercentage,
    Amount discountAmount,
    Amount total,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version) {
}
