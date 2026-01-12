package at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output.OpportunityOutput;
import at.backend.MarketingCompany.crm.opportunity.adapter.input.graphql.dto.output.OpportunityStatisticsResponse;
import at.backend.MarketingCompany.crm.opportunity.core.application.OpportunityQueryServiceImpl.OpportunityStatistics;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.shared.PageResponse;

@Component
public class OpportunityResponseMapper {

  public OpportunityOutput toOutput(Opportunity opportunity) {
    BigDecimal estimatedValue = null;
    if (opportunity.getAmount() != null) {
      estimatedValue = opportunity.getAmount().value();
    }

    String expectedCloseDate = null;
    if (opportunity.getExpectedCloseDate() != null) {
      expectedCloseDate = opportunity.getExpectedCloseDate().value() != null
          ? opportunity.getExpectedCloseDate().value().toString()
          : null;
    }

    String nextStepsDueDate = null;
    if (opportunity.getNextSteps() != null && opportunity.getNextSteps().hasDueDate()) {
      nextStepsDueDate = opportunity.getNextSteps().dueDate() != null
          ? opportunity.getNextSteps().dueDate().toString()
          : null;
    }

    return OpportunityOutput.builder()
        .id(opportunity.getId() != null ? opportunity.getId().asString() : null)
        .companyId(opportunity.getCustomerCompanyId() != null
            ? opportunity.getCustomerCompanyId().getValue().toString()
            : null)
        .title(opportunity.getTitle())
        .estimatedValue(estimatedValue)
        .stage(opportunity.getStage() != null ? opportunity.getStage().name() : null)
        .expectedCloseDate(expectedCloseDate)
        .isClosed(opportunity.isClosed())
        .isWon(opportunity.isWon())
        .isLost(opportunity.isLost())
        .isOverdue(opportunity.isOverdue())
        .lossReason(opportunity.getLossReason() != null ? opportunity.getLossReason().value() : null)
        .lossReasonDetails(opportunity.getLossReason() != null ? opportunity.getLossReason().details() : null)
        .nextSteps(opportunity.getNextSteps() != null ? opportunity.getNextSteps().value() : null)
        .nextStepsDueDate(nextStepsDueDate)
        .probability(opportunity.getProbability() != null ? opportunity.getProbability().value() : null)
        .createdAt(opportunity.getCreatedAt() != null ? opportunity.getCreatedAt().toString() : null)
        .updatedAt(opportunity.getUpdatedAt() != null ? opportunity.getUpdatedAt().toString() : null)
        .build();
  }

  public PageResponse<OpportunityOutput> toPageResponse(Page<Opportunity> opportunityPage) {
    if (opportunityPage == null)
      return PageResponse.empty();

    var responsePage = opportunityPage.map(this::toOutput);
    return PageResponse.of(responsePage);
  }

  public OpportunityStatisticsResponse toStatisticsResponse(
      OpportunityStatistics statistics) {
    if (statistics == null)
      return null;

    return new OpportunityStatisticsResponse(
        statistics.totalOpportunities(),
        statistics.activeOpportunities(),
        statistics.wonOpportunities(),
        statistics.lostOpportunities(),
        statistics.winRate(),
        BigDecimal.valueOf(statistics.totalPipelineValue()),
        BigDecimal.valueOf(statistics.averageDealSize()),
        statistics.prospectingStageCount(),
        statistics.QUALIFICATIONStageCount(),
        statistics.proposalStageCount(),
        statistics.negotiationStageCount());
  }
}
