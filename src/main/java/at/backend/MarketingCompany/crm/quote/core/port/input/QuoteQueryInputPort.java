package at.backend.MarketingCompany.crm.quote.core.port.input;

import java.util.List;

import org.springframework.data.domain.Page;

import at.backend.MarketingCompany.crm.quote.core.application.queries.GetAllQuotesQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetExpiredQuotesQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetQuoteByIdQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetQuotesByCustomerQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetQuotesByOpportunityQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetQuotesByStatusQuery;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;

public interface QuoteQueryInputPort {
  Page<Quote> getAllQuotes(GetAllQuotesQuery query);

  Quote getQuoteById(GetQuoteByIdQuery query);

  List<Quote> getQuotesByOpportunity(GetQuotesByOpportunityQuery query);

  Page<Quote> getQuotesByCustomer(GetQuotesByCustomerQuery query);

  Page<Quote> getQuotesByStatus(GetQuotesByStatusQuery query);

  List<Quote> getExpiredQuotes(GetExpiredQuotesQuery query);
}
