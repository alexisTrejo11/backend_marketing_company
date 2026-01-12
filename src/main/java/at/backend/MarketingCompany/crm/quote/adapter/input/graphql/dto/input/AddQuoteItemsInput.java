package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input;

import java.util.List;

import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.QuoteItemInput;
import at.backend.MarketingCompany.crm.quote.core.application.commands.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.QuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddQuoteItemsInput(
    @NotNull String quoteId,
    @NotEmpty List<QuoteItemInput> items) {
  public AddQuoteItemsCommand toCommand() {
    List<QuoteItemCommand> itemCommands = items.stream()
        .map(QuoteItemInput::toCommand)
        .toList();

    return new AddQuoteItemsCommand(
        QuoteId.of(quoteId),
        itemCommands);
  }
}
