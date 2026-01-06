package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.CloseOpportunityWonCommand;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CloseOpportunityWonInput(
    @NotBlank String opportunityId,
    @Size(max = 500) String closeNotes) {

  public CloseOpportunityWonCommand toCommand() {
    return new CloseOpportunityWonCommand(
        OpportunityId.of(opportunityId),
        closeNotes);
  }
}
