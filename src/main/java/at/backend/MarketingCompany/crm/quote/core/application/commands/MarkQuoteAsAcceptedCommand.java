package at.backend.MarketingCompany.crm.quote.core.application.commands;

import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import lombok.Builder;

@Builder
public record MarkQuoteAsAcceptedCommand(QuoteId quoteId) {
}
