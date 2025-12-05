package at.backend.MarketingCompany.crm.quote.application.output;

import at.backend.MarketingCompany.crm.quote.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuoteRepositoryPort {
    Quote save(Quote quote);
    Optional<Quote> findById(QuoteId id);
    Page<Quote> findAll(Pageable pageable);
    void delete(QuoteId id);
}