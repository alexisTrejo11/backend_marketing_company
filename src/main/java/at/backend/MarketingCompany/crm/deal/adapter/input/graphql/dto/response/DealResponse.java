package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DealResponse(
    String id,
    // String customerCompanyId,
    // String opportunityId,
    // String campaignManagerId,
    String dealStatus,
    BigDecimal finalAmount,
    LocalDate startDate,
    LocalDate endDate,
    String deliverables,
    String terms,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
  public record CustomerInfo(UUID id, String name, String email) {
  }

  public record OpportunityInfo(UUID id, String title, String stage) {
  }

  public record UserInfo(UUID id, String name, String email) {
  }

  public record ServicePackageInfo(UUID id, String name, String description) {
  }
}
