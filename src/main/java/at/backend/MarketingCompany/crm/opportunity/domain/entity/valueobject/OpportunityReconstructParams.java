package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;

import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OpportunityReconstructParams(
    OpportunityId id,
    CustomerId customerId,
    String title,
    OpportunityAmount amount,
    OpportunityStage stage,
    ExpectedCloseDate expectedCloseDate,
    Integer version,
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

