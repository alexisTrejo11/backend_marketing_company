package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;

import lombok.Builder;

import java.time.LocalDateTime;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

@Builder
public record OpportunityReconstructParams(
    OpportunityId id,
    CustomerId customerId,
    String title,
    Amount amount,
    OpportunityStage stage,
    ExpectedCloseDate expectedCloseDate,
    Integer version,
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
