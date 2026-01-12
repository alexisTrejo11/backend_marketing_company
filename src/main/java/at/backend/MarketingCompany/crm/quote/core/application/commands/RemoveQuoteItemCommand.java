package at.backend.MarketingCompany.crm.quote.core.application.commands;

import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import lombok.Builder;

@Builder
public record RemoveQuoteItemCommand(
    QuoteId quoteId,
    QuoteItemId itemId) {
}
