package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OpportunityResponse(
    String id,
    String customerId,
    CustomerInfo customer,
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
    LocalDateTime updatedAt
) {
    public record CustomerInfo(String id, String name, String email) {}
}


