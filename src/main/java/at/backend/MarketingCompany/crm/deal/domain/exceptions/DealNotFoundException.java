package at.backend.MarketingCompany.crm.deal.domain.exceptions;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;

public class DealNotFoundException extends RuntimeException {
    public DealNotFoundException(DealId dealId) {
        super(String.format("Deal with ID %s not found", dealId.value()));
    }
}
