package at.backend.MarketingCompany.crm.servicePackage.core.application.query;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

public record GetServicePackageQuery(ServicePackageId id) {
  public static GetServicePackageQuery of(String id) {
    return new GetServicePackageQuery(ServicePackageId.of(id));
  }
}
