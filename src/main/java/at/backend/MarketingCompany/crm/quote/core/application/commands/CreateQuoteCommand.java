package at.backend.MarketingCompany.crm.quote.core.application.commands;

import java.time.LocalDate;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.Builder;

@Builder
public record CreateQuoteCommand(
    CustomerCompanyId customerCompanyId,
    LocalDate validUntil,
    String notes,
    String termsAndConditions,
    OpportunityId opportunityId) {
}
