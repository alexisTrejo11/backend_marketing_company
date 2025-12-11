package at.backend.MarketingCompany.crm.quote.domain.valueobject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Discount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.domain.model.QuoteItem;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

@Builder
public record QuoteReconstructParams(
    QuoteId id,
    CustomerCompanyId customerCompanyId,
    OpportunityId opportunityId,
    LocalDate validUntil,
    Amount subTotal,
    Discount discount,
    Amount totalAmount,
    QuoteStatus status,
    List<QuoteItem> items,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version) {
}
