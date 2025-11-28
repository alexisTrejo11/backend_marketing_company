package at.backend.MarketingCompany.crm.tasks.application.queries;

import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import org.springframework.data.domain.Pageable;

public record GetTasksByCustomerQuery(CustomerId customerId, Pageable pageable) {}
