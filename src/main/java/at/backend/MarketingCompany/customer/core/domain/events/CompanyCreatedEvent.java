package at.backend.MarketingCompany.customer.core.domain.events;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyName;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyStatus;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.Industry;

public class CompanyCreatedEvent extends CompanyEvent {
    private final CompanyName companyName;
    private final Industry industry;
    private final CompanyStatus initialStatus;
    
    public CompanyCreatedEvent(CustomerCompanyId companyId, CompanyName companyName,
                               Industry industry, CompanyStatus initialStatus) {
        super(companyId);
        this.companyName = companyName;
        this.industry = industry;
        this.initialStatus = initialStatus;
    }
    
    public CompanyName getCompanyName() { return companyName; }
    public Industry getIndustry() { return industry; }
    public CompanyStatus getInitialStatus() { return initialStatus; }
}

