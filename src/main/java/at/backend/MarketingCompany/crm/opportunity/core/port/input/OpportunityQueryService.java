package at.backend.MarketingCompany.crm.opportunity.core.port.input;

import org.springframework.data.domain.Page;

import at.backend.MarketingCompany.crm.opportunity.core.application.OpportunityQueryServiceImpl.OpportunityStatistics;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetActiveOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetLostOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunitiesByCustomerQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunitiesByStageQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunitiesByStagesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunityByIdQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOpportunityStatisticsQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetOverdueOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.GetWonOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.application.queries.SearchOpportunitiesQuery;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;

public interface OpportunityQueryService {

  Opportunity getOpportunityById(GetOpportunityByIdQuery query);

  Page<Opportunity> getOpportunitiesByCustomer(GetOpportunitiesByCustomerQuery query);

  Page<Opportunity> getOpportunitiesByStage(GetOpportunitiesByStageQuery query);

  Page<Opportunity> getOpportunitiesByStages(GetOpportunitiesByStagesQuery query);

  Page<Opportunity> getActiveOpportunities(GetActiveOpportunitiesQuery query);

  Page<Opportunity> getOverdueOpportunities(GetOverdueOpportunitiesQuery query);

  Page<Opportunity> getWonOpportunities(GetWonOpportunitiesQuery query);

  Page<Opportunity> getLostOpportunities(GetLostOpportunitiesQuery query);

  Page<Opportunity> getOpportunities(SearchOpportunitiesQuery query);

  OpportunityStatistics getOpportunityStatistics(GetOpportunityStatisticsQuery query);
}
