package at.backend.MarketingCompany.crm.deal.application;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.CustomerId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.v2.domain.entity.valueobjects.ServicePackageId;

import java.util.List;

public interface ExternalModuleValidator {
    void validateCustomerExists(CustomerId customerId);
    void validateOpportunityExists(OpportunityId opportunityId);
    void validateEmployeeExists(EmployeeId employeeId);
    void validateServicesExist(List<ServicePackageId> serviceIds);
}