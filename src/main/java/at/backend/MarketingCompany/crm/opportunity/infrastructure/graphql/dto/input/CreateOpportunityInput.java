package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.application.commands.CreateOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityAmount;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateOpportunityInput(
    @NotBlank String customerId,
    @NotBlank String title,
    BigDecimal amount,
    LocalDate expectedCloseDate
) {
    public CreateOpportunityCommand toCommand() {
        return new CreateOpportunityCommand(
                CustomerId.of(customerId),
                title,
                OpportunityAmount.from(amount),
                ExpectedCloseDate.from(expectedCloseDate)
        );
    }
}

