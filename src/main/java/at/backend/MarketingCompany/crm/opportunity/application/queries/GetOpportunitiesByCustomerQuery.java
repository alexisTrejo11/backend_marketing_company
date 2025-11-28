package at.backend.MarketingCompany.crm.opportunity.application.queries;

import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import org.springframework.data.domain.Pageable;

public record GetOpportunitiesByCustomerQuery(CustomerId customerId, Pageable pageable) {}
