package at.backend.MarketingCompany.crm.quote.core.application.output;

import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuoteRepository {
  Quote save(Quote quote);

  Optional<Quote> findById(QuoteId id);

  Page<Quote> findAll(Pageable pageable);

  void delete(QuoteId id);
}
