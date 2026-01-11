package at.backend.MarketingCompany.crm.quote.core.application.commands;

import java.time.LocalDate;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import lombok.Builder;

@Builder
public record UpdateQuoteDetailsCommand(
    QuoteId quoteId,
    LocalDate validUntil,
    String notes,
    String termsAndConditions,
    OpportunityId opportunityId) {
}
