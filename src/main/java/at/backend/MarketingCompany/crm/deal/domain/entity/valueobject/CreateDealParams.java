package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Builder
public record CreateDealParams(
        CustomerId customerId,
        OpportunityId opportunityId,
        LocalDate startDate,
        List<ServicePackageId> servicePackageIds
) {
    
    public CreateDealParams {
        Objects.requireNonNull(customerId, "CustomerId must not be null");
        Objects.requireNonNull(opportunityId, "OpportunityId must not be null");
        Objects.requireNonNull(startDate, "StartDate must not be null");

        Objects.requireNonNull(servicePackageIds, "ServicePackageIds must not be null");
        if (servicePackageIds.isEmpty()) {
            throw new IllegalArgumentException("ServicePackageIds cannot be empty");
        }
        for (ServicePackageId svc : servicePackageIds) {
            Objects.requireNonNull(svc, "ServicePackageIds cannot contain null elements");
        }

        // defensive immutable copy
        servicePackageIds = List.copyOf(servicePackageIds);
    }
}
