package at.backend.MarketingCompany.crm.deal.application;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;

import java.util.List;

public interface ExternalModuleValidator {
    void validateCustomerExists(CustomerId customerId);
    void validateOpportunityExists(OpportunityId opportunityId);
    void validateEmployeeExists(EmployeeId employeeId);
    void validateServicesExist(List<ServicePackageId> serviceIds);
}