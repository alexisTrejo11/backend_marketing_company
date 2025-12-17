package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.CreateOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateOpportunityInput(
    @NotNull @Positive Long customerId,
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
