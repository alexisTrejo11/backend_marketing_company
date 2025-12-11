package at.backend.MarketingCompany.customer.domain.events;

import at.backend.MarketingCompany.customer.domain.valueobject.*;
import java.math.BigDecimal;
import java.util.Set;

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

