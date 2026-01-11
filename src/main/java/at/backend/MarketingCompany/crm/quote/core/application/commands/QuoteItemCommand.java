package at.backend.MarketingCompany.crm.quote.core.application.commands;

import java.math.BigDecimal;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

public record QuoteItemCommand(
    ServicePackageId servicePackageId) {

  public QuoteItem toQuoteItem(QuoteId quoteId, BigDecimal unitPrice) {
    return QuoteItem.create(
        quoteId,
        servicePackageId,
        Amount.create(unitPrice),
        Discount.random());
  }
}
