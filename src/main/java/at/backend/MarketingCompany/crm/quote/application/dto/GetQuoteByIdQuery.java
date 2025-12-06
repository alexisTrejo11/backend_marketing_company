package at.backend.MarketingCompany.crm.quote.application.dto;

import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;

public record GetQuoteByIdQuery(QuoteId id) {
  public static GetQuoteByIdQuery from(String id) {
    return new GetQuoteByIdQuery(new QuoteId(id));
  }
}
