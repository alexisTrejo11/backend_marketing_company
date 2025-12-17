package at.backend.MarketingCompany.crm.quote.adapter.output.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import at.backend.MarketingCompany.crm.quote.adapter.output.entity.QuoteItemEntity;
import at.backend.MarketingCompany.crm.quote.adapter.output.mapper.QuoteItemMapper;
import at.backend.MarketingCompany.crm.quote.core.port.output.QuoteItemRepository;
import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuoteItemRepositoryImpl implements QuoteItemRepository {
  private final QuoteItemJpaRepository quoteItemJpaRepository;
  private final QuoteItemMapper itemMapper;

  @Override
  public QuoteItem save(QuoteItem item) {
    QuoteItemEntity entity = itemMapper.toEntity(item);

    QuoteItemEntity savedEntity = quoteItemJpaRepository.save(entity);

    return itemMapper.toDomain(savedEntity);
  }

  @Override
  public void bulkSave(List<QuoteItem> items) {
    List<QuoteItemEntity> entities = items.stream()
        .map(itemMapper::toEntity)
        .toList();
    quoteItemJpaRepository.saveAll(entities);
  }

  @Override
  public Optional<QuoteItem> findById(QuoteItemId id) {
    Optional<QuoteItemEntity> entityOpt = quoteItemJpaRepository.findById(id.getValue());
    return entityOpt.map(itemMapper::toDomain);
  }

  @Override
  public void delete(QuoteItemId id) {
    quoteItemJpaRepository.deleteById(id.getValue());
  }

}
