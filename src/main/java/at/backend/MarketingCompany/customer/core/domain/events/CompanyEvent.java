package at.backend.MarketingCompany.customer.core.domain.events;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.domain.event.DomainEvent;

public abstract class CompanyEvent extends DomainEvent {
    protected final CustomerCompanyId companyId;
    
    protected CompanyEvent(CustomerCompanyId companyId) {
        super();
        this.companyId = companyId;
    }
    
    public CustomerCompanyId getCompanyId() {
        return companyId;
    }
}