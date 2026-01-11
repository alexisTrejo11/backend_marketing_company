package at.backend.MarketingCompany.crm.quote.core.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.crm.quote.core.application.queries.GetAllQuotesQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetExpiredQuotesQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetQuoteByIdQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetQuotesByCustomerQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetQuotesByOpportunityQuery;
import at.backend.MarketingCompany.crm.quote.core.application.queries.GetQuotesByStatusQuery;
import at.backend.MarketingCompany.crm.quote.core.domain.exception.QuoteNotFoundException;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.port.input.QuoteQueryInputPort;
import at.backend.MarketingCompany.crm.quote.core.port.output.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteQueryService implements QuoteQueryInputPort {
  private final QuoteRepository quoteRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Quote> getAllQuotes(GetAllQuotesQuery query) {
    log.debug("Getting all quotes with pageable: {}", query.pageable());
    return quoteRepository.findAll(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Quote getQuoteById(GetQuoteByIdQuery query) {
    log.debug("Getting quote by ID: {}", query.quoteId());
    return quoteRepository.findById(query.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + query.quoteId()));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Quote> getQuotesByCustomer(GetQuotesByCustomerQuery query) {
    log.debug("Getting quotes for customer: {}", query.customerCompanyId());
    return quoteRepository.findByCustomerId(query.customerCompanyId(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Quote> getQuotesByOpportunity(GetQuotesByOpportunityQuery query) {
    log.debug("Getting quotes for opportunity: {}", query.opportunityId());
    return quoteRepository.findByOpportunityId(query.opportunityId());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Quote> getQuotesByStatus(GetQuotesByStatusQuery query) {
    log.debug("Getting quotes by status: {}", query.status());
    return quoteRepository.findByStatus(query.status(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Quote> getExpiredQuotes(GetExpiredQuotesQuery query) {
    log.debug("Getting expired quotes");
    return quoteRepository.findExpiredQuotes();
  }
}
