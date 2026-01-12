package at.backend.MarketingCompany.crm.quote.core.domain.valueobject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

@Builder
public record QuoteReconstructParams(
    QuoteId id,
    CustomerCompanyId customerCompanyId,
    OpportunityId opportunityId,
    LocalDate validUntil,
    QuoteStatus status,
    List<QuoteItem> items,
    String notes,
    String termsAndConditions,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version) {
}
