package at.backend.MarketingCompany.crm.deal.application.commands;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.ServiceId;

import java.util.List;
import java.util.UUID;

public record UpdateDealServicesCommand(
    DealId dealId,
    List<ServiceId> servicePackageIds
) {
    public static UpdateDealServicesCommand from(UUID dealId, List<UUID> servicePackageIds) {
        DealId dId = new DealId(dealId.toString());
        List<ServiceId> serviceIds = servicePackageIds.stream()
                .map(ServiceId::from)
                .toList();
        return new UpdateDealServicesCommand(dId, serviceIds);
    }
}