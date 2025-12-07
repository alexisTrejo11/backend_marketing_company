package at.backend.MarketingCompany.crm.quote.application.dto;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

import java.time.LocalDate;
import java.util.List;

public record QuoteCreateCommand(
    CustomerId customerId,
    OpportunityId opportunityId,
    LocalDate validUntil,
    List<QuoteItemCreateCommand> items) {
}
