package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record UpdateOpportunityProbabilityCommand(
    OpportunityId opportunityId,
    int probability) {

  public UpdateOpportunityProbabilityCommand {
    if (opportunityId == null) {
      throw new IllegalArgumentException("OpportunityId cannot be null");
    }
  }

  public static UpdateOpportunityProbabilityCommand from(String opportunityId, int probability) {
    return new UpdateOpportunityProbabilityCommand(
        OpportunityId.of(opportunityId),
        probability);
  }
}
