package at.backend.MarketingCompany.crm.quote.core.application.input;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.crm.quote.core.application.dto.GetQuoteByIdQuery;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;

public interface QuoteQueryService {
  Page<Quote> getAllQuotes(Pageable pageable);

  Quote getQuoteById(GetQuoteByIdQuery query);
}
