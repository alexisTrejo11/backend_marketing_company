package at.backend.MarketingCompany.crm.deal.application;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalModuleValidatorImpl implements ExternalModuleValidator {
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
    public void validateServicesExist(List<ServicePackageId> serviceIds) {

    }
}
