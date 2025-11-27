package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.CustomerId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.OpportunityId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.ServiceId;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


public record CreateDealParams(
        CustomerId customerId,
        OpportunityId opportunityId,
        LocalDate startDate,
        List<ServiceId> servicePackageIds
) {
    @Builder
    public CreateDealParams {
        Objects.requireNonNull(customerId, "CustomerId must not be null");
        Objects.requireNonNull(opportunityId, "OpportunityId must not be null");
        Objects.requireNonNull(startDate, "StartDate must not be null");

        Objects.requireNonNull(servicePackageIds, "ServicePackageIds must not be null");
        if (servicePackageIds.isEmpty()) {
            throw new IllegalArgumentException("ServicePackageIds cannot be empty");
        }
        for (ServiceId svc : servicePackageIds) {
            Objects.requireNonNull(svc, "ServicePackageIds cannot contain null elements");
        }

        // defensive immutable copy
        servicePackageIds = List.copyOf(servicePackageIds);
    }
}
