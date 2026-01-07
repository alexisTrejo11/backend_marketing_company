package at.backend.MarketingCompany.crm.servicePackage.core.application.command;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

public record DeleteServicePackageCommand(ServicePackageId id) {

  public static DeleteServicePackageCommand of(Long id) {
    return new DeleteServicePackageCommand(new ServicePackageId(id));
  }
}
