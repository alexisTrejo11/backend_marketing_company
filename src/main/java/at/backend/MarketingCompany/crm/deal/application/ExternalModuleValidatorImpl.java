package at.backend.MarketingCompany.crm.deal.application;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.CustomerId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.OpportunityId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.ServiceId;

import java.util.List;

public class ExternalModuleValidatorImpl implements  ExternalModuleValidator {
    @Override
    public void validateCustomerExists(CustomerId customerId) {

    }

    @Override
    public void validateOpportunityExists(OpportunityId opportunityId) {

    }

    @Override
    public void validateEmployeeExists(EmployeeId employeeId) {

    }

    @Override
    public void validateServicesExist(List<ServiceId> serviceIds) {

    }
}
