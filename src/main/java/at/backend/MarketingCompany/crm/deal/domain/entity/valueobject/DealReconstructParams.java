package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.Utils.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.CustomerId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.v2.domain.entity.valueobjects.ServicePackageId;
import lombok.Builder;

import java.util.List;

@Builder
public record DealReconstructParams(
        DealId id,
        Integer version,
        java.time.LocalDateTime deletedAt,
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime updatedAt,
        CustomerId customerId,
        OpportunityId opportunityId,
        DealStatus dealStatus,
        FinalAmount finalAmount,
        ContractPeriod period,
        EmployeeId campaignManagerId,
        String deliverables,
        String terms,
        List<ServicePackageId>servicePackageIds
) {
}
