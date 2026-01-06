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
        .orElseThrow(() -> new OpportunityNotFoundException(query.opportunityId()));
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

    long totalOpportunities;
    long activeOpportunities;
    long wonOpportunities;
    long lostOpportunities;
    double winRate;
    double totalPipelineValue;
    double averageDealSize;
    long prospectingCount;
    long QUALIFICATIONCount;
    long proposalCount;
    long negotiationCount;

    if (query.customerCompanyId() != null) {
      // Filter by customer
      totalOpportunities = opportunityRepository.findByCustomer(query.customerCompanyId()).size();
      activeOpportunities = opportunityRepository.countActiveByCustomer(query.customerCompanyId());
      wonOpportunities = opportunityRepository.countByCustomerAndStage(
          query.customerCompanyId(), OpportunityStage.CLOSED_WON);
      lostOpportunities = opportunityRepository.countByCustomerAndStage(
          query.customerCompanyId(), OpportunityStage.CLOSED_LOST);
      winRate = opportunityRepository.calculateWinRateByCustomer(query.customerCompanyId());
      totalPipelineValue = opportunityRepository.calculateTotalPipelineValue(query.customerCompanyId());
      averageDealSize = opportunityRepository.calculateAverageDealSize(query.customerCompanyId());
      prospectingCount = opportunityRepository.countByCustomerAndStage(
          query.customerCompanyId(), OpportunityStage.PROSPECTING);
      QUALIFICATIONCount = opportunityRepository.countByCustomerAndStage(
          query.customerCompanyId(), OpportunityStage.QUALIFICATION);
      proposalCount = opportunityRepository.countByCustomerAndStage(
          query.customerCompanyId(), OpportunityStage.PROPOSAL);
      negotiationCount = opportunityRepository.countByCustomerAndStage(
          query.customerCompanyId(), OpportunityStage.NEGOTIATION);
    } else {
      // Global statistics (all customers)
      totalOpportunities = opportunityRepository.count();
      activeOpportunities = opportunityRepository.countActive();
      wonOpportunities = opportunityRepository.countByStage(OpportunityStage.CLOSED_WON);
      lostOpportunities = opportunityRepository.countByStage(OpportunityStage.CLOSED_LOST);
      winRate = opportunityRepository.calculateWinRate();
      totalPipelineValue = opportunityRepository.calculateTotalPipelineValue(null);
      averageDealSize = opportunityRepository.calculateAverageDealSize(null);
      prospectingCount = opportunityRepository.countByStage(OpportunityStage.PROSPECTING);
      QUALIFICATIONCount = opportunityRepository.countByStage(OpportunityStage.QUALIFICATION);
      proposalCount = opportunityRepository.countByStage(OpportunityStage.PROPOSAL);
      negotiationCount = opportunityRepository.countByStage(OpportunityStage.NEGOTIATION);
    }

    return new OpportunityStatistics(
        totalOpportunities,
        activeOpportunities,
        wonOpportunities,
        lostOpportunities,
        winRate,
        totalPipelineValue,
        averageDealSize,
        prospectingCount,
        QUALIFICATIONCount,
        proposalCount,
        negotiationCount);
  }

  public record OpportunityStatistics(
      long totalOpportunities,
      long activeOpportunities,
      long wonOpportunities,
      long lostOpportunities,
      double winRate,
      double totalPipelineValue,
      double averageDealSize,
      long prospectingStageCount,
      long QUALIFICATIONStageCount,
      long proposalStageCount,
      long negotiationStageCount) {
  }
}
