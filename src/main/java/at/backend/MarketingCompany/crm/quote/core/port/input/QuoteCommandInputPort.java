package at.backend.MarketingCompany.crm.quote.core.port.input;

import at.backend.MarketingCompany.crm.quote.core.application.commands.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.CreateQuoteCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.DeleteQuoteCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsAcceptedCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsRejectedCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsSentCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.RemoveQuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.UpdateQuoteDetailsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.UpdateQuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;

public interface QuoteCommandInputPort {
  Quote createQuote(CreateQuoteCommand command);

  Quote addQuoteItems(AddQuoteItemsCommand command);

  Quote updateQuoteItem(UpdateQuoteItemCommand command);

  Quote removeQuoteItem(RemoveQuoteItemCommand command);

  Quote markQuoteAsSent(MarkQuoteAsSentCommand command);

  Quote markQuoteAsAccepted(MarkQuoteAsAcceptedCommand command);

  Quote markQuoteAsRejected(MarkQuoteAsRejectedCommand command);

  Quote updateQuoteDetails(UpdateQuoteDetailsCommand command);

  void deleteQuote(DeleteQuoteCommand command);

  void markExpiredQuotes();
}
