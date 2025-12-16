package at.backend.MarketingCompany.customer.core.domain.events;

import at.backend.MarketingCompany.customer.core.domain.valueobject.AnnualRevenue;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public class CompanyUpgradedToEnterpriseEvent extends CompanyEvent {
    private final CompanySize oldSize;
    private final CompanySize newSize;
    private final AnnualRevenue revenue;
    
    public CompanyUpgradedToEnterpriseEvent(CustomerCompanyId companyId,
                                            CompanySize oldSize,
                                            CompanySize newSize,
                                            AnnualRevenue revenue) {
        super(companyId);
        this.oldSize = oldSize;
        this.newSize = newSize;
        this.revenue = revenue;
    }
    
    public CompanySize getOldSize() { return oldSize; }
    public CompanySize getNewSize() { return newSize; }
    public AnnualRevenue getRevenue() { return revenue; }
}
