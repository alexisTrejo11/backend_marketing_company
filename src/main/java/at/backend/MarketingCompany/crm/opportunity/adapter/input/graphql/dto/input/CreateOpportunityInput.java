package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.CreateOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
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
        new CustomerCompanyId(customerId),
        title,
        new Amount(amount),
        ExpectedCloseDate.from(expectedCloseDate));
  }
}
