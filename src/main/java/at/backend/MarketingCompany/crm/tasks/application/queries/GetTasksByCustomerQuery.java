package at.backend.MarketingCompany.crm.tasks.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record GetTasksByCustomerQuery(CustomerId customerId, Pageable pageable) {
}
