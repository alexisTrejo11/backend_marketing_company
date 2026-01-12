package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.crm.quote.core.application.commands.RemoveQuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import jakarta.validation.constraints.NotNull;

public record RemoveQuoteItemInput(
    @NotNull String quoteId,
    @NotNull String itemId) {
  public RemoveQuoteItemCommand toCommand() {
    return RemoveQuoteItemCommand.builder()
        .quoteId(QuoteId.of(quoteId))
        .itemId(QuoteItemId.of(itemId))
        .build();
  }
}
