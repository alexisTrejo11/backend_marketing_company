package at.backend.MarketingCompany.customer.core.domain.events;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyStatus;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public class CompanyStatusChangedEvent extends CompanyEvent {
    private final CompanyStatus oldStatus;
    private final CompanyStatus newStatus;
    private final String reason;
    
    public CompanyStatusChangedEvent(CustomerCompanyId companyId,
                                     CompanyStatus oldStatus,
                                     CompanyStatus newStatus,
                                     String reason) {
        super(companyId);
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.reason = reason;
    }
    
    public CompanyStatus getOldStatus() { return oldStatus; }
    public CompanyStatus getNewStatus() { return newStatus; }
    public String getReason() { return reason; }
}
