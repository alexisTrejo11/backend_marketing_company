package at.backend.MarketingCompany.customer.core.domain.events;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public class CompanyContactPersonAddedEvent extends CompanyEvent {
    private final String contactPersonName;
    private final String position;
    private final String email;
    private final boolean isDecisionMaker;
    
    public CompanyContactPersonAddedEvent(CustomerCompanyId companyId,
                                          String contactPersonName,
                                          String position,
                                          String email,
                                          boolean isDecisionMaker) {
        super(companyId);
        this.contactPersonName = contactPersonName;
        this.position = position;
        this.email = email;
        this.isDecisionMaker = isDecisionMaker;
    }
    
    public String getContactPersonName() { return contactPersonName; }
    public String getPosition() { return position; }
    public String getEmail() { return email; }
    public boolean isDecisionMaker() { return isDecisionMaker; }
}
