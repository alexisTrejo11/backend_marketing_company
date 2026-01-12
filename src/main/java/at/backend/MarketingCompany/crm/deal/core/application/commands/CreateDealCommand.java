package at.backend.MarketingCompany.crm.deal.core.application.commands;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.CreateDealParams;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

import java.time.LocalDate;
import java.util.List;

public record CreateDealCommand(
    CustomerCompanyId customerCompanyId,
    OpportunityId opportunityId,
    List<ServicePackageId> servicePackageIds,
    LocalDate startDate) {

  public static CreateDealCommand from(
      String customerId,
      String opportunityId,
      List<String> servicePackageIds,
      LocalDate startDate) {
    return new CreateDealCommand(
        customerId != null ? CustomerCompanyId.of(customerId) : null,
        opportunityId != null ? OpportunityId.of(opportunityId) : null,
        servicePackageIds != null
            ? servicePackageIds.stream().map(ServicePackageId::of).toList()
            : List.of(),
        startDate);
  }

  public CreateDealParams toCreateParams() {
    return CreateDealParams.builder()
        .customerCompanyId(customerCompanyId)
        .opportunityId(opportunityId)
        .startDate(startDate)
        .servicePackageIds(servicePackageIds)
        .build();
  }
}
