package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input;

import java.math.BigDecimal;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.core.application.commands.UpdateQuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import jakarta.validation.constraints.NotNull;

public record UpdateQuoteItemInput(
    @NotNull String quoteId,
    @NotNull String itemId,
    BigDecimal newUnitPrice,
    BigDecimal newDiscountPercentage) {
  public UpdateQuoteItemCommand toCommand() {
    return UpdateQuoteItemCommand.builder()
        .quoteId(QuoteId.of(quoteId))
        .itemId(QuoteItemId.of(itemId))
        .newUnitPrice(newUnitPrice != null ? Amount.create(newUnitPrice) : null)
        .newDiscount(newDiscountPercentage != null ? Discount.create(newDiscountPercentage) : null)
        .build();
  }
}
