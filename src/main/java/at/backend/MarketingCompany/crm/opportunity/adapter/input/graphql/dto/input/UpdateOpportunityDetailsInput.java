package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.UpdateOpportunityDetailsCommand;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateOpportunityDetailsInput(
    @NotNull String opportunityId,
    @NotBlank @Size(max = 200) String title,
    BigDecimal estimatedValue,
    LocalDate expectedCloseDate,
    @Size(max = 1000) String nextSteps,
    LocalDateTime nextStepsDueDate,
    @Min(0) @Max(100) Integer probability) {

  public UpdateOpportunityDetailsCommand toCommand() {
    return UpdateOpportunityDetailsCommand.from(
        opportunityId,
        title,
        estimatedValue,
        expectedCloseDate,
        nextSteps,
        nextStepsDueDate,
        probability);

  }
}
