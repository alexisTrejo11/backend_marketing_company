package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.core.application.commands.UpdateQuoteDetailsCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateQuoteDetailsInput(
    @NotNull String quoteId,
    String validUntil,
    @Size(max = 1000) String notes,
    @Size(max = 5000) String termsAndConditions,
    String opportunityId) {
  public UpdateQuoteDetailsCommand toCommand() {
    return UpdateQuoteDetailsCommand.builder()
        .quoteId(QuoteId.of(quoteId))
        .validUntil(validUntil != null ? LocalDate.parse(validUntil) : null)
        .notes(notes)
        .termsAndConditions(termsAndConditions)
        .opportunityId(opportunityId != null ? OpportunityId.of(opportunityId) : null)
        .build();
  }
}
