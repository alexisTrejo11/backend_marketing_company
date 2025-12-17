package at.backend.MarketingCompany.crm.quote.core.port.input;

import at.backend.MarketingCompany.crm.quote.core.application.dto.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.dto.QuoteCreateCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;

public interface QuoteCommandService {
  Quote createQuote(QuoteCreateCommand command);

  Quote addQuoteItems(AddQuoteItemsCommand command);

  Quote deleteQuoteItem(QuoteItemId itemId);

  Quote deleteQuote(QuoteId quoteId);
}
