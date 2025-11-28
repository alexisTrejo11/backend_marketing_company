package at.backend.MarketingCompany.crm.opportunity.domain.exceptions;

public class OpportunityNotFoundException extends RuntimeException {
    public OpportunityNotFoundException(String opportunityId) {
        super("Opportunity not found with ID: " + opportunityId);
    }
}

