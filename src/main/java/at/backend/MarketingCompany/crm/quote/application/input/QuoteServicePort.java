package at.backend.MarketingCompany.crm.quote.application.input;

import at.backend.MarketingCompany.crm.quote.application.dto.QuoteCommand;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteItemId;

public interface QuoteServicePort {
    Quote createQuote(QuoteCommand command);
    Quote addQuoteItem(QuoteId quoteId, QuoteItemCommand input);
    Quote removeQuoteItem(QuoteItemId itemId);
    Quote deleteQuote(QuoteId quoteId);
}