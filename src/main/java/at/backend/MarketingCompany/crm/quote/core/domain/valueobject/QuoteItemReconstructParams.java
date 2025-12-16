package at.backend.MarketingCompany.crm.quote.core.domain.valueobject;

import java.time.LocalDateTime;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Discount;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

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
