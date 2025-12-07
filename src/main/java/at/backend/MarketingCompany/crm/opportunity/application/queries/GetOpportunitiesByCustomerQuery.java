package at.backend.MarketingCompany.crm.opportunity.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record GetOpportunitiesByCustomerQuery(CustomerId customerId, Pageable pageable) {
}
