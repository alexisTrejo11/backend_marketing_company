package at.backend.MarketingCompany.crm.quote.core.domain.service;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class QuoteDomainService {

  public void validateQuoteDates(LocalDate validUntil) {
    LocalDate maxValidUntil = LocalDate.now().plusMonths(10);

    if (validUntil.isAfter(maxValidUntil)) {
      throw new IllegalArgumentException(
          "The 'valid until' date exceeds the allowed limit. The maximum duration is 10 months from today.");
    }
  }

  public QuoteItem createQuoteItem(
      Quote quote,
      ServicePackageId servicePackageId,
      Amount unitPrice,
      Discount discountPercentage) {
    return QuoteItem.create(
        quote.getId(),
        servicePackageId,
        unitPrice,
        discountPercentage);
  }
}
