package at.backend.MarketingCompany.crm.quote.application.input;

import at.backend.MarketingCompany.crm.quote.application.dto.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteCreateCommand;
import at.backend.MarketingCompany.crm.quote.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteItemId;

public interface QuoteServicePort {
  Quote handle(QuoteCreateCommand command);

  Quote handle(AddQuoteItemsCommand command);

  Quote handle(QuoteItemId itemId);

  Quote handle(QuoteId quoteId);
}
