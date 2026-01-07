package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record DealResponse(
    String id,
    String customerCompanyId,
    String opportunityId,
    String campaignManagerId,
    String status,
    BigDecimal finalAmount,
    LocalDate startDate,
    LocalDate endDate,
    String deliverables,
    String terms,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
