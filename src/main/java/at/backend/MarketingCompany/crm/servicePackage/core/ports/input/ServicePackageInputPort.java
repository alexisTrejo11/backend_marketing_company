package at.backend.MarketingCompany.crm.servicePackage.core.ports.input;

import org.springframework.data.domain.Page;

import at.backend.MarketingCompany.crm.servicePackage.core.application.command.CreateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.command.DeleteServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.command.UpdateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.query.GetAllServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.core.application.query.GetServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.ServicePackage;

public interface ServicePackageInputPort {
  ServicePackage createServicePackage(CreateServicePackageCommand command);

  ServicePackage updateServicePackage(UpdateServicePackageCommand command);

  ServicePackage deleteServicePackage(DeleteServicePackageCommand command);

  Page<ServicePackage> getAllServicePackage(GetAllServicePackageQuery query);

  ServicePackage getServicePackage(GetServicePackageQuery query);
}
