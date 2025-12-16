package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.shared.dto.PageInput;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunitiesByStageQuery;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;

public record GetOpportunitiesByStageInput(OpportunityStage stage, PageInput pageInput) {

  public GetOpportunitiesByStageQuery toQuery() {
    return new GetOpportunitiesByStageQuery(
        stage,
        pageInput.toPageable());
  }
}
