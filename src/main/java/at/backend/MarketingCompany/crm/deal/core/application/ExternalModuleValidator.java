package at.backend.MarketingCompany.crm.deal.core.application;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

import java.util.List;

public interface ExternalModuleValidator {
  void validateCustomerExists(CustomerCompanyId customerCompanyId);

  void validateOpportunityExists(OpportunityId opportunityId);

  void validateEmployeeExists(EmployeeId employeeId);

  void validateServicesExist(List<ServicePackageId> serviceIds);
}
