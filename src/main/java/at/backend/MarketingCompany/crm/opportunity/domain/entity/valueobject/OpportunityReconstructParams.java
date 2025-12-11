package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OpportunityReconstructParams(
    OpportunityId id,
    CustomerCompanyId customerCompanyId,
    String title,
    Amount amount,
    OpportunityStage stage,
    ExpectedCloseDate expectedCloseDate,
    Integer version,
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
