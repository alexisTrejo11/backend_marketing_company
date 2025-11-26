package at.backend.MarketingCompany.crm.deal.v2.application.dto;

import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.CustomerId;
import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.OpportunityId;
import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.ServiceId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DealResponse(
    DealId id,
    CustomerId customerId,
    OpportunityId opportunityId,
    String status,
    BigDecimal finalAmount,
    LocalDate startDate,
    LocalDate endDate,
    EmployeeId campaignManagerId,
    String deliverables,
    String terms,
    List<ServiceId> servicePackageIds,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
