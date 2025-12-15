package at.backend.MarketingCompany.crm.quote.core.application.dto;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;

import java.time.LocalDate;
import java.util.List;

public record QuoteCreateCommand(
    CustomerCompanyId customerCompanyId,
    OpportunityId opportunityId,
    LocalDate validUntil,
    List<QuoteItemCreateCommand> items) {
}
