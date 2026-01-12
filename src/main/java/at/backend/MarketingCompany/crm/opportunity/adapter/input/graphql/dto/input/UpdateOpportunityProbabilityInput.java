package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.UpdateOpportunityProbabilityCommand;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateOpportunityProbabilityInput(
    @NotNull String opportunityId,
    @Min(0) @Max(100) int probability) {

  public UpdateOpportunityProbabilityCommand toCommand() {
    return UpdateOpportunityProbabilityCommand.from(
        opportunityId,
        probability);
  }
}
