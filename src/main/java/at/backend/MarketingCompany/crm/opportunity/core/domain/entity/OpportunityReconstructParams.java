package at.backend.MarketingCompany.crm.opportunity.core.domain.entity;

import java.time.LocalDateTime;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.LossReason;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.NextSteps;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Probability;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

@Builder
public record OpportunityReconstructParams(
    OpportunityId id,
    CustomerCompanyId customerCompanyId,
    String title,
    Amount amount,
    OpportunityStage stage,
    ExpectedCloseDate expectedCloseDate,
    Integer version,
    LossReason lossReason,
    NextSteps nextSteps,
    Probability probability,
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
