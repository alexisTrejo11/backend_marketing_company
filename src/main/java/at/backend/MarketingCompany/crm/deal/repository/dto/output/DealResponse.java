package at.backend.MarketingCompany.crm.deal.repository.dto.output;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record DealResponse(
    String id,
    String customerId,
    CustomerInfo customer,
    String opportunityId,
    OpportunityInfo opportunity,
    String dealStatus,
    BigDecimal finalAmount,
    LocalDate startDate,
    LocalDate endDate,
    String campaignManagerId,
    UserInfo campaignManager,
    List<ServicePackageInfo> services,
    String deliverables,
    String terms,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public record CustomerInfo(UUID id, String name, String email) {}
    public record OpportunityInfo(UUID id, String title, String stage) {}  
    public record UserInfo(UUID id, String name, String email) {}
    public record ServicePackageInfo(UUID id, String name, String description) {}
}