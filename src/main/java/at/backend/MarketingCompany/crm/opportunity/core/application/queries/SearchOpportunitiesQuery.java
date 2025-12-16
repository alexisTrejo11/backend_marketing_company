package at.backend.MarketingCompany.crm.opportunity.core.application.queries;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public record SearchOpportunitiesQuery(
    String searchTerm,
    Set<OpportunityStage> stages,
    CustomerCompanyId customerCompanyId,
    Pageable pageable) {
  public static SearchOpportunitiesQuery empty() {
    return new SearchOpportunitiesQuery(
        "",
        Set.of(),
        null,
        Pageable.unpaged());
  }
}
