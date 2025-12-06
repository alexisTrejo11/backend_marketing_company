package at.backend.MarketingCompany.crm.quote.application.output;

import at.backend.MarketingCompany.crm.quote.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteItemId;

import java.util.List;
import java.util.Optional;

public interface QuoteItemRepositoryPort {
  QuoteItem save(QuoteItem item);

  void bulkSave(List<QuoteItem> items);

  Optional<QuoteItem> findById(QuoteItemId id);

  void delete(QuoteItemId id);
}
