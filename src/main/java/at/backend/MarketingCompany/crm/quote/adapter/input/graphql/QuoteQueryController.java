package at.backend.MarketingCompany.crm.quote.adapter.input.graphql;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.mapper.QuoteOutputMapper;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteStatus;
import at.backend.MarketingCompany.crm.quote.core.port.input.QuoteQueryInputPort;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.output.QuoteOutput;
import at.backend.MarketingCompany.crm.quote.core.application.queries.*;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuoteQueryController {
  private final QuoteOutputMapper responseMapper;
  private final QuoteQueryInputPort queryService;

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public PageResponse<QuoteOutput> quotes(@Argument @Valid @NotNull PageInput input) {
    log.debug("Fetching all quotes with pagination");
    var query = new GetAllQuotesQuery(input.toPageable());
    var quotes = queryService.getAllQuotes(query);
    return responseMapper.toOutputPage(quotes);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public QuoteOutput quote(@Argument String quoteId) {
    log.debug("Fetching quote by ID: {}", quoteId);
    var query = GetQuoteByIdQuery.from(quoteId);
    Quote quote = queryService.getQuoteById(query);
    return responseMapper.toOutput(quote);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public PageResponse<QuoteOutput> quotesByCustomer(
      @Argument String customerId,
      @Argument @Valid @NotNull PageInput pageInput) {
    log.debug("Fetching quotes for customer: {}", customerId);
    var query = GetQuotesByCustomerQuery.builder()
        .customerCompanyId(
            CustomerCompanyId.of(customerId))
        .pageable(pageInput.toPageable())
        .build();
    Page<Quote> quotes = queryService
        .getQuotesByCustomer(query);
    return responseMapper.toOutputPage(quotes);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public List<QuoteOutput> quotesByOpportunity(@Argument String opportunityId) {
    log.debug("Fetching quotes for opportunity: {}", opportunityId);
    var query = GetQuotesByOpportunityQuery.from(opportunityId);
    List<Quote> quotes = queryService.getQuotesByOpportunity(query);
    return responseMapper.toOutputList(quotes);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public PageResponse<QuoteOutput> quotesByStatus(
      @Argument QuoteStatus status,
      @Argument @Valid @NotNull PageInput pageInput) {
    log.debug("Fetching quotes by status: {}", status);
    var query = new GetQuotesByStatusQuery(status, pageInput.toPageable());
    var quotes = queryService.getQuotesByStatus(query);
    return responseMapper.toOutputPage(quotes);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public List<QuoteOutput> expiredQuotes() {
    log.debug("Fetching expired quotes");
    var query = new GetExpiredQuotesQuery();
    var quotes = queryService.getExpiredQuotes(query);
    return responseMapper.toOutputList(quotes);
  }
}
