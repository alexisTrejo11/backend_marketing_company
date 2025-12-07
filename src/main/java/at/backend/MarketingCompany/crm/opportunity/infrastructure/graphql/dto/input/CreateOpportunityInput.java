package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.application.commands.CreateOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateOpportunityInput(
    @NotBlank String customerId,
    @NotBlank String title,
    BigDecimal amount,
    LocalDate expectedCloseDate) {
  public CreateOpportunityCommand toCommand() {
    return new CreateOpportunityCommand(
        CustomerId.of(customerId),
        title,
        new Amount(amount),
        ExpectedCloseDate.from(expectedCloseDate));
  }
}
