package at.backend.MarketingCompany.crm.quote.adapter.output.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.adapter.output.entity.QuoteEntity;
import at.backend.MarketingCompany.crm.quote.adapter.output.mapper.QuoteJpaEntityMapper;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteStatus;
import at.backend.MarketingCompany.crm.quote.core.port.output.QuoteRepository;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuoteRepositoryImpl implements QuoteRepository {
  private final QuoteJpaRepository quoteJpaRepository;
  private final QuoteJpaEntityMapper quoteJpaEntityMapper;

  @Override
  public Quote save(Quote quote) {
    QuoteEntity entity = quoteJpaEntityMapper.toEntity(quote);
    entity.processNewEntityIfNeeded();
    QuoteEntity savedEntity = quoteJpaRepository.saveAndFlush(entity);
    return quoteJpaEntityMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Quote> findById(QuoteId id) {
    return quoteJpaRepository.findById(id.getValue())
        .map(quoteJpaEntityMapper::toDomain);
  }

  @Override
  public Page<Quote> findAll(Pageable pageable) {
    return quoteJpaRepository.findAll(pageable)
        .map(quoteJpaEntityMapper::toDomain);
  }

  @Override
  public void delete(QuoteId id) {
    quoteJpaRepository.deleteById(id.getValue());
  }

  @Override
  public Page<Quote> findByCustomerId(CustomerCompanyId customerCompanyId, Pageable pageable) {
    return quoteJpaRepository.findByCustomerCompanyId(customerCompanyId.getValue(), pageable)
        .map(quoteJpaEntityMapper::toDomain);
  }

  @Override
  public List<Quote> findByOpportunityId(OpportunityId opportunityId) {
    return quoteJpaRepository.findByOpportunityId(opportunityId.getValue()).stream()
        .map(quoteJpaEntityMapper::toDomain)
        .toList();
  }

  @Override
  public Page<Quote> findByStatus(QuoteStatus status, Pageable pageable) {
    return quoteJpaRepository.findByStatus(status, pageable)
        .map(quoteJpaEntityMapper::toDomain);
  }

  @Override
  public List<Quote> findExpiredQuotes() {
    return quoteJpaRepository.findExpiredQuotes().stream()
        .map(quoteJpaEntityMapper::toDomain)
        .toList();
  }
}
