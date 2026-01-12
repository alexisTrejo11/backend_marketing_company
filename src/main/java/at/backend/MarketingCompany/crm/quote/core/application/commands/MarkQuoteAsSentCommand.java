package at.backend.MarketingCompany.crm.quote.core.application.commands;

import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;

public record MarkQuoteAsSentCommand(QuoteId quoteId) {
}
