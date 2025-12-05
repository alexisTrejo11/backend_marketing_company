package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence;

import at.backend.MarketingCompany.crm.quote.application.output.QuoteRepositoryPort;
import at.backend.MarketingCompany.crm.quote.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.entity.QuoteEntity;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.mapper.QuoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QuotePersistenceAdapter implements QuoteRepositoryPort {
    private final QuoteJpaRepository quoteJpaRepository;
    private final QuoteMapper quoteMapper;
    
    @Override
    public Quote save(Quote quote) {
        QuoteEntity entity = quoteMapper.toEntity(quote);
        QuoteEntity savedEntity = quoteJpaRepository.save(entity);
        return quoteMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Quote> findById(QuoteId id) {
        return quoteJpaRepository.findById(id.value())
            .map(quoteMapper::toDomain);
    }
    
    @Override
    public Page<Quote> findAll(Pageable pageable) {
        return quoteJpaRepository.findAll(pageable)
            .map(quoteMapper::toDomain);
    }
    
    @Override
    public void delete(QuoteId id) {
        quoteJpaRepository.deleteById(id.value());
    }
}