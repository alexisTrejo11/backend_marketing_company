package at.backend.MarketingCompany.crm.opportunity.infrastructure.persistence;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.*;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import org.springframework.stereotype.Component;


@Component
public class OpportunityEntityMapper {

    public OpportunityEntity toEntity(Opportunity opportunity) {
        if (opportunity == null) return null;

        OpportunityEntity entity = new OpportunityEntity();
        
        // ID
        if (opportunity.getId() != null) {
            entity.setId(opportunity.getId().value());
        }
        
        // Basic fields
        entity.setTitle(opportunity.getTitle());
        entity.setStage(opportunity.getStage());
        
        // Optional fields
        opportunity.getAmount().ifPresent(amount -> entity.setAmount(amount.value()));
        opportunity.getExpectedCloseDate().ifPresent(closeDate -> 
            entity.setExpectedCloseDate(closeDate.value()));
        
        // Audit fields
        entity.setCreatedAt(opportunity.getCreatedAt());
        entity.setUpdatedAt(opportunity.getUpdatedAt());
        entity.setDeletedAt(opportunity.getDeletedAt());
        entity.setVersion(opportunity.getVersion());
        
        // Relations - solo IDs
        if (opportunity.getCustomerId() != null) {
            var customer = new CustomerModel(opportunity.getCustomerId().value());
            entity.setCustomerModel(customer);
        }

        return entity;
    }

    public Opportunity toDomain(OpportunityEntity entity) {
        if (entity == null) return null;

        var reconstructParams = OpportunityReconstructParams.builder()
            .id(OpportunityId.from(entity.getId()))
            .customerId(entity.getCustomerModel() != null ? 
                new CustomerId(entity.getCustomerModel().getId().toString()) : null)
            .title(entity.getTitle())
            .amount(entity.getAmount() != null ? 
                new OpportunityAmount(entity.getAmount()) : null)
            .stage(entity.getStage())
            .expectedCloseDate(entity.getExpectedCloseDate() != null ? 
                ExpectedCloseDate.from(entity.getExpectedCloseDate()) : null)
            .version(entity.getVersion())
            .deletedAt(entity.getDeletedAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();

        return Opportunity.reconstruct(reconstructParams);
    }

    public void updateEntity(OpportunityEntity existingEntity, Opportunity opportunity) {
        existingEntity.setTitle(opportunity.getTitle());
        existingEntity.setStage(opportunity.getStage());
        
        opportunity.getAmount().ifPresentOrElse(
            amount -> existingEntity.setAmount(amount.value()),
            () -> existingEntity.setAmount(null)
        );
        
        opportunity.getExpectedCloseDate().ifPresentOrElse(
            closeDate -> existingEntity.setExpectedCloseDate(closeDate.value()),
            () -> existingEntity.setExpectedCloseDate(null)
        );
        
    }
}