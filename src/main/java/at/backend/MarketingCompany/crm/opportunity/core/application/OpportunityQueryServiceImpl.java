package at.backend.MarketingCompany.crm.opportunity.core.application;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions.OpportunityNotFoundException;
import at.backend.MarketingCompany.crm.opportunity.core.port.input.OpportunityQueryService;
import at.backend.MarketingCompany.crm.opportunity.core.port.output.OpportunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpportunityQueryServiceImpl implements OpportunityQueryService {
  private final OpportunityRepository opportunityRepository;

  @Override
  @Transactional(readOnly = true)
  public Opportunity getOpportunityById(GetOpportunityByIdQuery query) {
    log.debug("Fetching opportunity by ID: {}", query.opportunityId());

    return opportunityRepository.findById(query.opportunityId())
        .orElseThrow(() -> new OpportunityNotFoundException(query.opportunityId().value()));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> getOpportunitiesByCustomer(GetOpportunitiesByCustomerQuery query) {
    log.debug("Fetching opportunities for customer: {}", query.customerCompanyId());

    return opportunityRepository.findByCustomer(query.customerCompanyId(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> getOpportunitiesByStage(GetOpportunitiesByStageQuery query) {
    log.debug("Fetching opportunities by stage: {}", query.stage());

    return opportunityRepository.findByStage(query.stage(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> getOpportunitiesByStages(GetOpportunitiesByStagesQuery query) {
    log.debug("Fetching opportunities by stages: {}", query.stages());

    return opportunityRepository.findByStages(query.stages(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> getActiveOpportunities(GetActiveOpportunitiesQuery query) {
    log.debug("Fetching active opportunities");

    return opportunityRepository.findActiveOpportunities(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> getOverdueOpportunities(GetOverdueOpportunitiesQuery query) {
    log.debug("Fetching overdue opportunities");

    return opportunityRepository.findOverdueOpportunities(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> getWonOpportunities(GetWonOpportunitiesQuery query) {
    log.debug("Fetching won opportunities");

    return opportunityRepository.findWonOpportunities(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> getLostOpportunities(GetLostOpportunitiesQuery query) {
    log.debug("Fetching lost opportunities");

    return opportunityRepository.findLostOpportunities(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Opportunity> getOpportunities(SearchOpportunitiesQuery query) {
    log.debug("Searching opportunities with criteria: {}", query);

    if (query.stages() != null && !query.stages().isEmpty()) {
      return opportunityRepository.findByStages(query.stages(), query.pageable());
    }

    if (query.customerCompanyId() != null) {
      return opportunityRepository.findByCustomer(query.customerCompanyId(), query.pageable());
    }

    return opportunityRepository.findActiveOpportunities(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public OpportunityStatistics getOpportunityStatistics(GetOpportunityStatisticsQuery query) {
    log.debug("Fetching opportunity statistics for customer: {}", query.customerCompanyId());

    long totalOpportunities = opportunityRepository.findByCustomer(query.customerCompanyId()).size();
    long activeOpportunities = opportunityRepository.countActiveByCustomer(query.customerCompanyId());
    long wonOpportunities = opportunityRepository.countByCustomerAndStage(query.customerCompanyId(),
        OpportunityStage.CLOSED_WON);
    long lostOpportunities = opportunityRepository.countByCustomerAndStage(query.customerCompanyId(),
        OpportunityStage.CLOSED_LOST);
    double winRate = opportunityRepository.calculateWinRateByCustomer(query.customerCompanyId());

    return new OpportunityStatistics(
        totalOpportunities,
        activeOpportunities,
        wonOpportunities,
        lostOpportunities,
        winRate);
  }

  public record OpportunityStatistics(
      long totalOpportunities,
      long activeOpportunities,
      long wonOpportunities,
      long lostOpportunities,
      double winRate) {
  }
}
