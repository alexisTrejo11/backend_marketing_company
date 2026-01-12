package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.ReopenOpportunityCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReopenOpportunityInput(
    @NotNull String opportunityId,
    @NotBlank @Size(max = 500) String reason) {
  public ReopenOpportunityCommand toCommand() {
    return ReopenOpportunityCommand.from(opportunityId, reason);
  }
}
