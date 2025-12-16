package at.backend.MarketingCompany.crm.deal.core.application.commands;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

import java.util.List;
import java.util.UUID;

public record UpdateDealServicesCommand(
    DealId dealId,
    List<ServicePackageId> servicePackageIds) {
  public static UpdateDealServicesCommand from(UUID dealId, List<UUID> servicePackageIds) {
    DealId dId = new DealId(dealId.toString());
    List<ServicePackageId> serviceIds = servicePackageIds.stream()
        .map(ServicePackageId::of)
        .toList();
    return new UpdateDealServicesCommand(dId, serviceIds);
  }
}
