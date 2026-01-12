package at.backend.MarketingCompany.crm.quote.core.application.queries;

import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;

public record GetQuoteByIdQuery(QuoteId quoteId) {
  public static GetQuoteByIdQuery from(String quoteId) {
    return new GetQuoteByIdQuery(QuoteId.of(quoteId));
  }
}
