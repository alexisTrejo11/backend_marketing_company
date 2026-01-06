package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record OpportunityOutput(
    String id,
    String companyId,
    String title,
    BigDecimal estimatedValue,
    String stage,
    String expectedCloseDate,
    Boolean isClosed,
    Boolean isWon,
    Boolean isLost,
    Boolean isOverdue,
    String lossReason,
    String lossReasonDetails,
    String nextSteps,
    String nextStepsDueDate,
    Integer probability,
    String createdAt,
    String updatedAt) {
}
