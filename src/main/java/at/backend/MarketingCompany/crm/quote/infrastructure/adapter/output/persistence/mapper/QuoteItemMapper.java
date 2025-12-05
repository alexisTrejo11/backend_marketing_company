package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.mapper;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.domain.model.*;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.entity.QuoteItemEntity;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import org.springframework.stereotype.Component;


@Component
public class QuoteItemMapper {

    public QuoteItem toDomain(QuoteItemEntity entity) {
        if (entity == null) {
            return null;
        }
        
        QuoteItem item = QuoteItem.create(
            new QuoteId(entity.getQuote().getId()),
            new ServicePackageId(entity.getServicePackage().getId()),
            new Amount(entity.getUnitPrice()),
            new Discount(entity.getDiscountPercentage())
        );
        
        if (entity.getId() != null) {
            setId(item, entity.getId());
        }
        
        setCalculatedAmounts(item, entity);
        setTimestamps(item, entity.getCreatedAt(), entity.getUpdatedAt());
        
        return item;
    }
    

    public QuoteItemEntity toEntity(QuoteItem domain) {
        if (domain == null) {
            return null;
        }
        
        QuoteItemEntity entity = new QuoteItemEntity();
        
        if (domain.getId() != null) {
            entity.setId(domain.getId().value());
        }
        
        // Nota: Quote y ServicePackageEntity se deben establecer desde el adaptador
        
        entity.setUnitPrice(domain.getUnitPrice().value());
        entity.setTotal(domain.getTotal().value());
        entity.setDiscountPercentage(domain.getDiscountPercentage().percentage());
        entity.setDiscount(domain.getDiscountAmount().value());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        
        return entity;
    }
    



    private void setId(QuoteItem item, String id) {
        try {
            var field = QuoteItem.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(item, new QuoteItemId(id));
        } catch (Exception e) {
            throw new RuntimeException("Error setting ID on QuoteItem", e);
        }
    }
    
    private void setCalculatedAmounts(QuoteItem item, QuoteItemEntity entity) {
        try {
            var totalField = QuoteItem.class.getDeclaredField("total");
            var discountAmountField = QuoteItem.class.getDeclaredField("discountAmount");
            
            totalField.setAccessible(true);
            discountAmountField.setAccessible(true);
            
            totalField.set(item, new Amount(entity.getTotal()));
            discountAmountField.set(item, new Amount(entity.getDiscount()));
        } catch (Exception e) {
            throw new RuntimeException("Error setting calculated amounts on QuoteItem", e);
        }
    }
    
    private void setTimestamps(QuoteItem item, java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt) {
        try {
            var createdAtField = QuoteItem.class.getDeclaredField("createdAt");
            var updatedAtField = QuoteItem.class.getDeclaredField("updatedAt");
            
            createdAtField.setAccessible(true);
            updatedAtField.setAccessible(true);
            
            createdAtField.set(item, createdAt);
            updatedAtField.set(item, updatedAt);
        } catch (Exception e) {
            throw new RuntimeException("Error setting timestamps on QuoteItem", e);
        }
    }
    
    /**
     * Versión alternativa usando un método de reconstrucción
     */
    public QuoteItem reconstructDomain(
        String id,
        QuoteId quoteId,
        ServicePackageId servicePackageId,
        Amount unitPrice,
        Amount total,
        Discount discountPercentage,
        Amount discountAmount,
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime updatedAt
    ) {
        QuoteItem item = QuoteItem.create(quoteId, servicePackageId, unitPrice, discountPercentage);
        
        try {
            // Establecer ID
            var idField = QuoteItem.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(item, new QuoteItemId(id));
            
            // Establecer valores calculados
            var totalField = QuoteItem.class.getDeclaredField("total");
            var discountAmountField = QuoteItem.class.getDeclaredField("discountAmount");
            var createdAtField = QuoteItem.class.getDeclaredField("createdAt");
            var updatedAtField = QuoteItem.class.getDeclaredField("updatedAt");
            
            totalField.setAccessible(true);
            discountAmountField.setAccessible(true);
            createdAtField.setAccessible(true);
            updatedAtField.setAccessible(true);
            
            totalField.set(item, total);
            discountAmountField.set(item, discountAmount);
            createdAtField.set(item, createdAt);
            updatedAtField.set(item, updatedAt);
            
        } catch (Exception e) {
            throw new RuntimeException("Error reconstructing QuoteItem domain", e);
        }
        
        return item;
    }
}