package at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions;

public class OpportunityNotFoundException extends RuntimeException {
  public OpportunityNotFoundException(String opportunityId) {
    super("Opportunity not found with ID: " + opportunityId);
  }
}
