package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsSentCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import jakarta.validation.constraints.NotNull;

public record MarkQuoteAsSentInput(@NotNull String quoteId) {

  public MarkQuoteAsSentCommand toCommand() {
    return new MarkQuoteAsSentCommand(QuoteId.of(quoteId));
  }
}
