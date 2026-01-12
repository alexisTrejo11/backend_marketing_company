package at.backend.MarketingCompany.crm.quote.core.port.output;

import java.util.List;
import java.util.Optional;

import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;

public interface QuoteItemRepository {
  QuoteItem save(QuoteItem item);

  void bulkSave(List<QuoteItem> items);

  Optional<QuoteItem> findById(QuoteItemId id);

  void delete(QuoteItemId id);
}
