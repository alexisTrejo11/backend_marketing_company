package at.backend.MarketingCompany.crm.opportunity.application.queries;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public record SearchOpportunitiesQuery(
    String searchTerm,
    Set<OpportunityStage> stages,
    CustomerId customerId,
    Pageable pageable
) {}
