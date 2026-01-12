package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsAcceptedCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import jakarta.validation.constraints.NotNull;

public record MarkQuoteAsAcceptedInput(
    @NotNull String quoteId) {
  public MarkQuoteAsAcceptedCommand toCommand() {
    return MarkQuoteAsAcceptedCommand.builder()
        .quoteId(QuoteId.of(quoteId))
        .build();
  }
}
