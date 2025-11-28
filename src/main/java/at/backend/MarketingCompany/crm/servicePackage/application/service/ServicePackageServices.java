package at.backend.MarketingCompany.crm.servicePackage.application.service;

import at.backend.MarketingCompany.crm.servicePackage.application.dto.ServicePackageResponse;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.command.CreateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.command.DeleteServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.command.UpdateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.query.GetAllServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.query.GetServicePackageQuery;
import org.springframework.data.domain.Page;

public interface ServicePackageServices {
    ServicePackageResponse handle(CreateServicePackageCommand command);
    ServicePackageResponse handle(UpdateServicePackageCommand command);
    ServicePackageResponse handle(DeleteServicePackageCommand command);

    Page<ServicePackageResponse> handle(GetAllServicePackageQuery query);
    ServicePackageResponse handle(GetServicePackageQuery query);
}

