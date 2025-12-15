package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;

public record OpportunityResponse(
    String id,
    String customerId,
    String title,
    BigDecimal amount,
    OpportunityStage stage,
    LocalDate expectedCloseDate,
    boolean isClosed,
    boolean isWon,
    boolean isLost,
    boolean isOverdue,
    boolean canBeModified,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {

}
