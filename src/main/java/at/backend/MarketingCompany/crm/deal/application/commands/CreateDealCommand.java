package at.backend.MarketingCompany.crm.deal.application.commands;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateDealCommand(
    CustomerId customerId,
    OpportunityId opportunityId,
    List<ServicePackageId> servicePackageIds,
    LocalDate startDate) {
  public static CreateDealCommand from(
      UUID customerId,
      UUID opportunityId,
      List<String> servicePackageIds,
      LocalDate startDate) {
    return new CreateDealCommand(
        new CustomerId(customerId.toString()),
        new OpportunityId(opportunityId.toString()),
        servicePackageIds.stream()
            .map(ServicePackageId::new)
            .toList(),
        startDate);
  }
}
