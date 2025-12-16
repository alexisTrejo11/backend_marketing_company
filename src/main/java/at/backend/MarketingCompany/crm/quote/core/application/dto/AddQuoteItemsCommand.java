package at.backend.MarketingCompany.crm.quote.core.application.dto;

import java.util.ArrayList;
import java.util.List;

import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

public record AddQuoteItemsCommand(QuoteId quoteId, List<QuoteItemCreateCommand> input) {

  public static AddQuoteItemsCommand from(String quoteId, List<QuoteItemCreateCommand> input) {
    return new AddQuoteItemsCommand(new QuoteId(quoteId), input);
  }

  public List<ServicePackageId> getServicePackageIds() {
    if (input == null || input.isEmpty()) {
      return List.of();
    }

    List<ServicePackageId> servicePackageIds = new ArrayList<>();

    for (QuoteItemCreateCommand item : input) {
      if (item.servicePackageId() == null)
        continue;

      servicePackageIds.add(item.servicePackageId());
    }
    return servicePackageIds;
  }
}
