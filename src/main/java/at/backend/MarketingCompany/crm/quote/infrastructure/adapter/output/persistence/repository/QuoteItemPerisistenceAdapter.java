package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import at.backend.MarketingCompany.crm.quote.application.output.QuoteItemRepositoryPort;
import at.backend.MarketingCompany.crm.quote.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.entity.QuoteItemEntity;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.mapper.QuoteItemMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuoteItemPerisistenceAdapter implements QuoteItemRepositoryPort {
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
    Optional<QuoteItemEntity> entityOpt = quoteItemJpaRepository.findById(id.value());
    return entityOpt.map(itemMapper::toDomain);
  }

  @Override
  public void delete(QuoteItemId id) {
    quoteItemJpaRepository.deleteById(id.value());
  }

}
