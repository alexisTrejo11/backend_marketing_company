package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input;

import java.util.List;

import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.QuoteItemInput;
import at.backend.MarketingCompany.crm.quote.core.application.dto.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.dto.QuoteItemCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddQuotesItemsInput(
    @NotBlank String id,
    @NotNull @Size(min = 1) List<QuoteItemInput> input) {

  public List<QuoteItemCreateCommand> toItemsCommands() {
    return input.stream()
        .map(QuoteItemInput::toCommand)
        .toList();
  }

  public AddQuoteItemsCommand toCommand() {
    var itemsCommands = toItemsCommands();
    return AddQuoteItemsCommand.from(id, itemsCommands);
  }
}
