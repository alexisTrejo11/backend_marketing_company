package at.backend.MarketingCompany.crm.tasks.application;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

import java.util.List;

public interface ExternalServiceValidator {
    void validateCustomerExists(CustomerId customerId);
    void validateOpportunityExists(OpportunityId opportunityId);
    void validateEmployeeExists(EmployeeId employeeId);
    
    void validateCustomersExist(List<CustomerId> customerIds);
    void validateEmployeesExist(List<EmployeeId> employeeIds);
}