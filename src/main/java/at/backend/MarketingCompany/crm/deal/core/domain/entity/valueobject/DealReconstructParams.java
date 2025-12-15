package at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record DealReconstructParams(
    DealId id,
    Integer version,
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    CustomerCompanyId customerCompanyId,
    OpportunityId opportunityId,
    DealStatus dealStatus,
    FinalAmount finalAmount,
    ContractPeriod period,
    EmployeeId campaignManagerId,
    String deliverables,
    String terms,
    List<ServicePackageId> servicePackageIds) {
}
