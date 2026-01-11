package at.backend.MarketingCompany.crm.quote.core.application.commands;

import java.util.ArrayList;
import java.util.List;

import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

public record AddQuoteItemsCommand(QuoteId quoteId, List<QuoteItemCommand> items) {
  public AddQuoteItemsCommand {
    if (quoteId == null) {
      throw new IllegalArgumentException("quoteId cannot be null");
    }

    if (items == null) {
      items = new ArrayList<>();
    }
  }

  public static AddQuoteItemsCommand from(String quoteId, List<QuoteItemCommand> items) {
    return new AddQuoteItemsCommand(QuoteId.of(quoteId), items);
  }

  public List<ServicePackageId> getServicePackageIds() {
    if (items == null || items.isEmpty()) {
      return List.of();
    }

    List<ServicePackageId> servicePackageIds = new ArrayList<>();

    for (QuoteItemCommand item : items) {
      if (item.servicePackageId() == null)
        continue;

      servicePackageIds.add(item.servicePackageId());
    }
    return servicePackageIds;
  }

}
