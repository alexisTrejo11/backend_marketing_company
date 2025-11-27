package at.backend.MarketingCompany.crm.deal.application.queries;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.CustomerId;

import java.util.UUID;

public record GetDealsByCustomerQuery(CustomerId customerId) {
    public static GetDealsByCustomerQuery from(UUID id) {
        return new GetDealsByCustomerQuery(new CustomerId(id));
    }
}
