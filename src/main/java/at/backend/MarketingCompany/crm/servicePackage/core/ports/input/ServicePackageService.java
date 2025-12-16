package at.backend.MarketingCompany.crm.servicePackage.core.ports.input;

import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.CreateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.DeleteServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.UpdateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.query.GetAllServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.query.GetServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.ServicePackage;
import org.springframework.data.domain.Page;

public interface ServicePackageService {
    ServicePackage createServicePackage(CreateServicePackageCommand command);
    ServicePackage updateServicePackage(UpdateServicePackageCommand command);
    ServicePackage deleteServicePackage(DeleteServicePackageCommand command);

    Page<ServicePackage> getAllServicePackage(GetAllServicePackageQuery query);
    ServicePackage getServicePackage(GetServicePackageQuery query);
}

