package at.backend.MarketingCompany.crm.deal.application;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

import java.util.List;

public interface ExternalModuleValidator {
  void validateCustomerExists(CustomerId customerId);

  void validateOpportunityExists(OpportunityId opportunityId);

  void validateEmployeeExists(EmployeeId employeeId);

  void validateServicesExist(List<ServicePackageId> serviceIds);
}
