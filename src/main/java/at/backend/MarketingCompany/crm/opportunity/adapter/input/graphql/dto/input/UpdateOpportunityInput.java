package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.UpdateOpportunityDetailsCommand;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateOpportunityInput(
    @NotBlank String opportunityId,
    @NotBlank String title,
    BigDecimal amount,
    LocalDate expectedCloseDate) {
  public UpdateOpportunityDetailsCommand toCommand() {
    return new UpdateOpportunityDetailsCommand(
        OpportunityId.from(opportunityId),
        title,
        new Amount(amount),
        ExpectedCloseDate.from(expectedCloseDate));
  }
}
