package at.backend.MarketingCompany.crm.deal.application.commands;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.CustomerId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.OpportunityId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.ServiceId;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateDealCommand(
    CustomerId customerId,
    OpportunityId opportunityId,
    List<ServiceId> servicePackageIds,
    LocalDate startDate
) {
    public static CreateDealCommand from(
        UUID customerId,
        UUID opportunityId,
        List<String> servicePackageIds,
        LocalDate startDate
    ) {
        return new CreateDealCommand(
            new CustomerId(customerId),
            new OpportunityId(opportunityId),
            servicePackageIds.stream()
                .map(ServiceId::new)
                .toList(),
            startDate
        );
    }
}