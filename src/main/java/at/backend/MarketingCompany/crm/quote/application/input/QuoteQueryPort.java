package at.backend.MarketingCompany.crm.quote.application.input;

import at.backend.MarketingCompany.crm.quote.application.dto.GetQuoteByIdQuery;
import at.backend.MarketingCompany.crm.quote.domain.model.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuoteQueryPort {
  Page<Quote> handle(Pageable pageable);

  Quote handle(GetQuoteByIdQuery query);
}
