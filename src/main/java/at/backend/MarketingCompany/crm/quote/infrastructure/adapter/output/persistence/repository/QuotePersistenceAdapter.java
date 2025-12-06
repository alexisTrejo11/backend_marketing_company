package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.repository;

import at.backend.MarketingCompany.crm.quote.application.output.QuoteRepositoryPort;
import at.backend.MarketingCompany.crm.quote.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.entity.QuoteEntity;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.mapper.QuoteJpaEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QuotePersistenceAdapter implements QuoteRepositoryPort {
  private final QuoteJpaRepository quoteJpaRepository;
  private final QuoteJpaEntityMapper quoteJpaEntityMapper;

  @Override
  public Quote save(Quote quote) {
    QuoteEntity entity = quoteJpaEntityMapper.toEntity(quote);
    QuoteEntity savedEntity = quoteJpaRepository.save(entity);
    return quoteJpaEntityMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Quote> findById(QuoteId id) {
    return quoteJpaRepository.findById(id.value())
        .map(quoteJpaEntityMapper::toDomain);
  }

  @Override
  public Page<Quote> findAll(Pageable pageable) {
    return quoteJpaRepository.findAll(pageable)
        .map(quoteJpaEntityMapper::toDomain);
  }

  @Override
  public void delete(QuoteId id) {
    quoteJpaRepository.deleteById(id.value());
  }
}
