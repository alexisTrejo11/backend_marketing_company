package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.mapper;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.domain.model.*;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.*;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.entity.QuoteEntity;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuoteMapper {
    private final QuoteItemMapper quoteItemMapper;
    
    public QuoteMapper(QuoteItemMapper quoteItemMapper) {
        this.quoteItemMapper = quoteItemMapper;
    }
    

    public Quote toDomain(QuoteEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Quote quote = Quote.create(
            new CustomerId(entity.getCustomer().getId()),
            entity.getValidUntil()
        );
        
        if (entity.getId() != null) {
            setId(quote, entity.getId());
        }
        
        if (entity.getOpportunity() != null) {
            quote.setOpportunity(new OpportunityId(entity.getOpportunity().getId()));
        }
        
        setAmounts(quote, entity);
        setStatus(quote, entity.getStatus());
        setTimestamps(quote, entity.getCreatedAt(), entity.getUpdatedAt());
        
        if (entity.getItems() != null) {
            List<QuoteItem> items = entity.getItems().stream()
                .map(quoteItemMapper::toDomain)
                .collect(Collectors.toList());
            setItems(quote, items);
        }
        
        return quote;
    }

    public QuoteEntity toEntity(Quote domain) {
        if (domain == null) {
            return null;
        }
        
        QuoteEntity entity = new QuoteEntity();
        
        if (domain.getId() != null) {
            entity.setId(domain.getId().value());
        }
        
        // Nota: CustomerEntity y OpportunityEntity se deben establecer desde el adaptador
        // ya que necesitan ser cargados desde la base de datos
        
        entity.setValidUntil(domain.getValidUntil());
        entity.setSubTotal(domain.getSubTotal().value());
        entity.setDiscount(domain.getDiscount().percentage());
        entity.setTotalAmount(domain.getTotalAmount().value());
        entity.setStatus(toEntityStatus(domain.getStatus()));
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        
        // Los items se manejan por separado en el adaptador
        
        return entity;
    }
    
    s
    
    private void setId(Quote quote, String id) {
        try {
            var field = Quote.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(quote, new QuoteId(id));
        } catch (Exception e) {
            throw new RuntimeException("Error setting ID on Quote", e);
        }
    }
    
    private void setAmounts(Quote quote, QuoteEntity entity) {
        try {
            var subTotalField = Quote.class.getDeclaredField("subTotal");
            var discountField = Quote.class.getDeclaredField("discount");
            var totalAmountField = Quote.class.getDeclaredField("totalAmount");
            
            subTotalField.setAccessible(true);
            discountField.setAccessible(true);
            totalAmountField.setAccessible(true);
            
            subTotalField.set(quote, new Amount(entity.getSubTotal()));
            discountField.set(quote, new Discount(entity.getDiscount()));
            totalAmountField.set(quote, new Amount(entity.getTotalAmount()));
        } catch (Exception e) {
            throw new RuntimeException("Error setting amounts on Quote", e);
        }
    }
    
    private void setStatus(Quote quote, at.backend.MarketingCompany.crm.shared.enums.QuoteStatus entityStatus) {
        try {
            var field = Quote.class.getDeclaredField("status");
            field.setAccessible(true);
            field.set(quote, toDomainStatus(entityStatus));
        } catch (Exception e) {
            throw new RuntimeException("Error setting status on Quote", e);
        }
    }
    
    private void setTimestamps(Quote quote, java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt) {
        try {
            var createdAtField = Quote.class.getDeclaredField("createdAt");
            var updatedAtField = Quote.class.getDeclaredField("updatedAt");
            
            createdAtField.setAccessible(true);
            updatedAtField.setAccessible(true);
            
            createdAtField.set(quote, createdAt);
            updatedAtField.set(quote, updatedAt);
        } catch (Exception e) {
            throw new RuntimeException("Error setting timestamps on Quote", e);
        }
    }
    
    private void setItems(Quote quote, List<QuoteItem> items) {
        try {
            var field = Quote.class.getDeclaredField("items");
            field.setAccessible(true);
            field.set(quote, items);
        } catch (Exception e) {
            throw new RuntimeException("Error setting items on Quote", e);
        }
    }

    public Quote reconstructDomain(
        Long id,
        CustomerId customerId,
        OpportunityId opportunityId,
        java.time.LocalDate validUntil,
        Amount subTotal,
        Discount discount,
        Amount totalAmount,
        QuoteStatus status,
        List<QuoteItem> items,
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime updatedAt
    ) {
        Quote quote = Quote.create(customerId, validUntil);
        
        setId(quote, id);
        
        try {
            var opportunityIdField = Quote.class.getDeclaredField("opportunityId");
            opportunityIdField.setAccessible(true);
            opportunityIdField.set(quote, opportunityId);
            
            var subTotalField = Quote.class.getDeclaredField("subTotal");
            var discountField = Quote.class.getDeclaredField("discount");
            var totalAmountField = Quote.class.getDeclaredField("totalAmount");
            var statusField = Quote.class.getDeclaredField("status");
            var itemsField = Quote.class.getDeclaredField("items");
            var createdAtField = Quote.class.getDeclaredField("createdAt");
            var updatedAtField = Quote.class.getDeclaredField("updatedAt");
            
            subTotalField.setAccessible(true);
            discountField.setAccessible(true);
            totalAmountField.setAccessible(true);
            statusField.setAccessible(true);
            itemsField.setAccessible(true);
            createdAtField.setAccessible(true);
            updatedAtField.setAccessible(true);
            
            subTotalField.set(quote, subTotal);
            discountField.set(quote, discount);
            totalAmountField.set(quote, totalAmount);
            statusField.set(quote, status);
            itemsField.set(quote, items != null ? items : new java.util.ArrayList<>());
            createdAtField.set(quote, createdAt);
            updatedAtField.set(quote, updatedAt);
            
        } catch (Exception e) {
            throw new RuntimeException("Error reconstructing Quote domain", e);
        }
        
        return quote;
    }
}