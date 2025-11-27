package at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external;

import java.util.UUID;

public record OpportunityId(UUID value) {
    public OpportunityId {
        if (value == null) {
            throw new IllegalArgumentException("Deal ID cannot be null.");
        }
    }

    public static OpportunityId create() {
        return new OpportunityId(UUID.randomUUID());
    }
}