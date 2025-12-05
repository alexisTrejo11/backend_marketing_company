package at.backend.MarketingCompany.crm.quote.application.input;

import at.backend.MarketingCompany.crm.quote.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface QuoteQueryPort {
    Page<Quote> getAllQuotes(Pageable pageable);
    Quote getQuoteById(QuoteId id);
}
