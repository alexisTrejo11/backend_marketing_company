package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsRejectedCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MarkQuoteAsRejectedInput(
    @NotNull String quoteId,
    @NotNull @Size(max = 500) String rejectionReason) {
  public MarkQuoteAsRejectedCommand toCommand() {
    return new MarkQuoteAsRejectedCommand(
        QuoteId.of(quoteId),
        rejectionReason);
  }
}
