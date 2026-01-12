package at.backend.MarketingCompany.crm.quote.adapter.output.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.adapter.output.entity.QuoteEntity;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteReconstructParams;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

@Component
public class QuoteJpaEntityMapper {
  private final QuoteItemMapper quoteItemMapper;

  @Autowired
  public QuoteJpaEntityMapper(QuoteItemMapper quoteItemMapper) {
    this.quoteItemMapper = quoteItemMapper;
  }

  public Quote toDomain(QuoteEntity entity) {
    if (entity == null)
      return null;

    return Quote.reconstruct(
        QuoteReconstructParams.builder()
            .id(entity.getId() != null ? new QuoteId(entity.getId()) : null)
            .customerCompanyId(
                entity.getCustomerCompany() != null ? new CustomerCompanyId(entity.getCustomerCompany().getId()) : null)
            .validUntil(entity.getValidUntil())
            .status(entity.getStatus())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .deletedAt(entity.getDeletedAt())
            .items(entity.getItems() != null ? entity.getItems().stream()
                .map(quoteItemMapper::toDomain)
                .toList() : List.of())
            .version(entity.getVersion())
            .build());
  }

  public QuoteEntity toEntity(Quote domain) {
    if (domain == null) {
      return null;
    }

    QuoteEntity entity = new QuoteEntity();
    entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
    entity.setCustomerCompany(
        domain.getCustomerCompanyId() != null ? new CustomerCompanyEntity(domain.getCustomerCompanyId().getValue())
            : null);
    entity.setValidUntil(domain.getValidUntil());
    entity.setStatus(domain.getStatus());
    entity.setCreatedAt(domain.getCreatedAt());
    entity.setUpdatedAt(domain.getUpdatedAt());
    entity.setDeletedAt(domain.getDeletedAt());
    entity.setVersion(domain.getVersion());

    return entity;
  }
}
