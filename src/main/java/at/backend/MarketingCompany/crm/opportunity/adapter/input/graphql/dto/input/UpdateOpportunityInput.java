package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.UpdateOpportunityDetailsCommand;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateOpportunityInput(
    @NotNull @Positive Long opportunityId,
    @NotBlank String title,
    BigDecimal amount,
    LocalDate expectedCloseDate) {
  public UpdateOpportunityDetailsCommand toCommand() {
    return new UpdateOpportunityDetailsCommand(
        new OpportunityId(opportunityId),
        title,
        new Amount(amount),
        ExpectedCloseDate.from(expectedCloseDate));
  }
}
