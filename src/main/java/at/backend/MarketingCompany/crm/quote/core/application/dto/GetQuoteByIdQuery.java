package at.backend.MarketingCompany.crm.quote.core.application.dto;

import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;

public record GetQuoteByIdQuery(QuoteId id) {
  public static GetQuoteByIdQuery from(String id) {
    return new GetQuoteByIdQuery(QuoteId.of(id));
  }
}
