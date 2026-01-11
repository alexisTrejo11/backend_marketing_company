package at.backend.MarketingCompany.crm.quote.adapter.output.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.adapter.output.entity.QuoteEntity;
import at.backend.MarketingCompany.crm.quote.adapter.output.entity.QuoteItemEntity;
import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemReconstructParams;
import at.backend.MarketingCompany.crm.servicePackage.adapter.output.model.ServicePackageEntity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

@Component
public class QuoteItemMapper {

  public QuoteItem toDomain(QuoteItemEntity entity) {
    if (entity == null) {
      return null;
    }

    var params = QuoteItemReconstructParams.builder()
        .id(entity.getId() != null ? new QuoteItemId(entity.getId()) : null)
        .unitPrice(entity.getUnitPrice() != null ? new Amount(entity.getUnitPrice()) : null)
        .discountPercentage(Discount.create(entity.getDiscountPercentage()))
        .discountAmount(entity.getDiscount() != null ? new Amount(entity.getDiscount()) : null)
        .total(entity.getTotal() != null ? new Amount(entity.getTotal()) : null)
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .deletedAt(entity.getDeletedAt())
        .version(entity.getVersion())
        .quoteId(
            entity.getQuote() != null
                ? new QuoteId(entity.getQuote().getId())
                : null)
        .servicePackageId(
            entity.getServicePackage() != null
                ? new ServicePackageId(entity.getServicePackage().getId())
                : null)
        .build();

    return QuoteItem.reconstruct(params);
  }

  public QuoteItemEntity toEntity(QuoteItem domain) {
    if (domain == null) {
      return null;
    }

    QuoteItemEntity entity = new QuoteItemEntity();
    entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
    entity.setUnitPrice(domain.getUnitPrice().value());
    entity.setTotal(domain.getTotal().value());
    entity.setDiscountPercentage(domain.getDiscountPercentage().percentage());
    entity.setDiscount(domain.getDiscountAmount().value());
    entity.setCreatedAt(domain.getCreatedAt());
    entity.setUpdatedAt(domain.getUpdatedAt());
    entity.setServicePackage(
        domain.getServicePackageId() != null
            ? new ServicePackageEntity(domain.getServicePackageId().getValue())
            : null);
    entity.setQuote(
        domain.getQuoteId() != null
            ? new QuoteEntity(domain.getQuoteId().getValue())
            : null);

    return entity;
  }

  public List<QuoteItemEntity> toEntities(List<QuoteItem> items) {
    if (items == null) {
      return List.of();
    }
    return items.stream()
        .map(this::toEntity)
        .toList();
  }
}
