package at.backend.MarketingCompany.customer.domain.events;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;

import java.math.BigDecimal;

public class CompanyOpportunityAddedEvent extends CompanyEvent {
    private final OpportunityId opportunityId;
    private final BigDecimal estimatedValue;
    private final String opportunityType;
    
    public CompanyOpportunityAddedEvent(CustomerCompanyId companyId,
                                        OpportunityId opportunityId,
                                        BigDecimal estimatedValue,
                                        String opportunityType) {
        super(companyId);
        this.opportunityId = opportunityId;
        this.estimatedValue = estimatedValue;
        this.opportunityType = opportunityType;
    }
    
    public OpportunityId getOpportunityId() { return opportunityId; }
    public BigDecimal getEstimatedValue() { return estimatedValue; }
    public String getOpportunityType() { return opportunityType; }
}
