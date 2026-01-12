package at.backend.MarketingCompany.crm.quote.core.port.output;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteStatus;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

public interface QuoteRepository {
  Quote save(Quote quote);

  Optional<Quote> findById(QuoteId id);

  Page<Quote> findAll(Pageable pageable);

  Page<Quote> findByCustomerId(CustomerCompanyId customerCompanyId, Pageable pageable);

  List<Quote> findByOpportunityId(OpportunityId opportunityId);

  Page<Quote> findByStatus(QuoteStatus status, Pageable pageable);

  List<Quote> findExpiredQuotes();

  void delete(QuoteId id);
}
