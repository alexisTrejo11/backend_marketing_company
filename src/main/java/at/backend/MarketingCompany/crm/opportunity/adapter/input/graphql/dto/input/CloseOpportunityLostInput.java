package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.CloseOpportunityLostCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CloseOpportunityLostInput(
    @NotNull String opportunityId,
    @NotBlank @Size(max = 100) String lossReason,
    @Size(max = 500) String lossReasonDetails,
    @Size(max = 500) String notes) {

  public CloseOpportunityLostCommand toCommand() {
    return CloseOpportunityLostCommand.from(
        opportunityId,
        lossReason,
        lossReasonDetails,
        notes);
  }
}
