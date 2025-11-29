package at.backend.MarketingCompany.crm.deal.application.queries;

import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

import java.util.UUID;

public record GetDealsByCustomerQuery(CustomerId customerId) {
    public static GetDealsByCustomerQuery from(UUID id) {
        return new GetDealsByCustomerQuery(new CustomerId(id.toString()));
    }
}
