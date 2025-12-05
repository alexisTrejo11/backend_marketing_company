package at.backend.MarketingCompany.crm.quote.application.dto;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.Opportunity;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

import java.time.LocalDate;
import java.util.List;

public record QuoteCommand(
    CustomerId customerId,
    Opportunity opportunityId,
    LocalDate validUntil,
    List<QuoteItemCommand> items
) {}