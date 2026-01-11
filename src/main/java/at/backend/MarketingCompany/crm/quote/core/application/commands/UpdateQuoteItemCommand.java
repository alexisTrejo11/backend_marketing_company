package at.backend.MarketingCompany.crm.quote.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import lombok.Builder;

@Builder
public record UpdateQuoteItemCommand(
    QuoteId quoteId,
    QuoteItemId itemId,
    Amount newUnitPrice,
    Discount newDiscount) {
}
