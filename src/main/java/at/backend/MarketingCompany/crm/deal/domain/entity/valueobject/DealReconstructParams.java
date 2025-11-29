package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
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
                CustomerId customerId,
                OpportunityId opportunityId,
                DealStatus dealStatus,
                FinalAmount finalAmount,
                ContractPeriod period,
                EmployeeId campaignManagerId,
                String deliverables,
                String terms,
                List<ServicePackageId> servicePackageIds) {
}
