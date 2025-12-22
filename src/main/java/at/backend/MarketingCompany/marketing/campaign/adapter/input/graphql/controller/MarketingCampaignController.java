package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.mapper.MarketingOutputMapper;
import at.backend.MarketingCompany.marketing.campaign.core.application.CampaignPerformanceSummary;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.AddCampaignSpendingCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.CreateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.UpdateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.query.CampaignQuery;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignPeriod;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.input.CampaignCommandServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.ports.input.CampaignQueryServicePort;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MarketingCampaignController {
	private final CampaignCommandServicePort commandServicePort;
  private final CampaignQueryServicePort queryServicePort;
  private final MarketingOutputMapper campaignMapper;


  @QueryMapping
  public PageResponse<CampaignOutput> campaigns(@Argument @Valid @NotNull PageInput pageInput) {
    log.debug("GraphQL Query: campaigns with page: {}, size: {}", 
        pageInput.page(), pageInput.size());
    
    Page<MarketingCampaign> campaignPage = queryServicePort.getAllCampaigns(pageInput.toPageable());
    
    return campaignMapper.toPageResponse(campaignPage);
  }

  @QueryMapping
  public CampaignOutput campaign(@Argument @Valid @NotNull @Positive Long id) {
    log.debug("GraphQL Query: campaign with id: {}", id);

	  var campaignId = new MarketingCampaignId(id);
    MarketingCampaign campaign = queryServicePort.getCampaignById(campaignId);
    
    return campaignMapper.toOutput(campaign);
  }

  @QueryMapping
  public PageResponse<CampaignOutput> searchCampaigns(
      @Argument @Valid @NotNull CampaignFilterInput filter,
      @Argument @Valid @NotNull PageInput pageInput) {

    log.debug("GraphQL Query: searchCampaigns with filter: {}", filter);
    CampaignQuery query = buildCampaignQuery(filter);

    Page<MarketingCampaign> campaignPage = queryServicePort.searchCampaigns(query, pageInput.toPageable());
    return campaignMapper.toPageResponse(campaignPage);
  }

  @QueryMapping
  public PageResponse<CampaignOutput> campaignsByStatus(
      @Argument @Valid @NotNull CampaignStatus status,
      @Argument @NotNull PageInput pageInput) {
    log.debug("GraphQL Query: campaignsByStatus with status: {}", status);
    Page<MarketingCampaign> campaignPage = queryServicePort.getCampaignsByStatus(
        status, 
        pageInput.toPageable()
    );
    
    return campaignMapper.toPageResponse(campaignPage);
  }

  @QueryMapping
  public PageResponse<CampaignOutput> expiredActiveCampaigns(@Argument @Valid @NotNull PageInput pageInput) {
    log.debug("GraphQL Query: expiredActiveCampaigns");
    Page<MarketingCampaign> campaignPage = queryServicePort.getExpiredActiveCampaigns(
        pageInput.toPageable()
    );
    
    return campaignMapper.toPageResponse(campaignPage);
  }

  @QueryMapping
  public PageResponse<CampaignOutput> campaignsNeedingOptimization(@Argument PageInput pageInput) {
    log.debug("GraphQL Query: campaignsNeedingOptimization");
    Page<MarketingCampaign> campaignPage = queryServicePort.getCampaignsNeedingOptimization(
        pageInput.toPageable()
    );
    
    return campaignMapper.toPageResponse(campaignPage);
  }

  @QueryMapping
  public PageResponse<CampaignOutput> highPerformingCampaigns(@Argument PageInput pageInput) {
    log.debug("GraphQL Query: highPerformingCampaigns");
    Page<MarketingCampaign> campaignPage = queryServicePort.getHighPerformingCampaigns(
        pageInput.toPageable()
    );
    
    return campaignMapper.toPageResponse(campaignPage);
  }


  @MutationMapping
  public CampaignOutput createCampaign(@Argument @Valid CreateCampaignInput input) {
    log.debug("GraphQL Mutation: createCampaign with name: {}", input.name());
    
    CreateCampaignCommand command = input.toCommand();
    MarketingCampaign campaign = commandServicePort.createCampaign(command);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput updateCampaign(@Argument @Valid UpdateCampaignInput input) {
    log.debug("GraphQL Mutation: updateCampaign with id: {}", input.id());
    
    UpdateCampaignCommand command = input.toCommand();
    MarketingCampaign campaign = commandServicePort.updateCampaign(command);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput launchCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: launchCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
    MarketingCampaign campaign = commandServicePort.launchCampaign(campaignId);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput pauseCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: pauseCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
    MarketingCampaign campaign = commandServicePort.pauseCampaign(campaignId);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput resumeCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: resumeCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
    MarketingCampaign campaign = commandServicePort.resumeCampaign(campaignId);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput completeCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: completeCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
	  MarketingCampaign campaign = commandServicePort.completeCampaign(campaignId);

		return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput cancelCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: cancelCampaign with id: {}", id);

	  var campaignId = new MarketingCampaignId(id);
	  MarketingCampaign campaign = commandServicePort.cancelCampaign(campaignId);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput addCampaignSpending(@Argument @Valid @NotNull AddSpendingInput input) {
    log.debug("GraphQL Mutation: addCampaignSpending with id: {}, amount: {}", 
        input.campaignId(), input.amount());

    var command = new AddCampaignSpendingCommand(
        new MarketingCampaignId(input.campaignId()),
        BigDecimal.valueOf(input.amount()),
        input.description()
    );
    
    MarketingCampaign campaign = commandServicePort.addSpending(command);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput updateCampaignRevenue(@Argument @Valid UpdateRevenueInput input) {
    log.debug("GraphQL Mutation: updateCampaignRevenue with id: {}, revenue: {}", 
        input.campaignId(), input.revenue());
    
    MarketingCampaign campaign = commandServicePort.updateAttributedRevenue(
        new MarketingCampaignId(input.campaignId()),
        BigDecimal.valueOf(input.revenue())
    );
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public boolean deleteCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: deleteCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
    commandServicePort.deleteCampaign(campaignId);
    
    return true;
  }

  private CampaignQuery buildCampaignQuery(CampaignFilterInput filter) {
    if (filter == null) {
      return CampaignQuery.empty();
    }

    return new CampaignQuery(
        filter.statuses() != null && !filter.statuses().isEmpty() ? 
            filter.statuses().getFirst() : null,
        filter.startDateFrom() != null ? LocalDate.parse(filter.startDateFrom()) : null,
        filter.startDateTo() != null ? LocalDate.parse(filter.startDateTo()) : null,
        filter.endDateFrom() != null ? LocalDate.parse(filter.endDateFrom()) : null,
        filter.endDateTo() != null ? LocalDate.parse(filter.endDateTo()) : null,
        filter.minBudget() != null ? BigDecimal.valueOf(filter.minBudget()) : null,
        filter.maxBudget() != null ? BigDecimal.valueOf(filter.maxBudget()) : null,
        filter.isActive(),
        filter.searchTerm()
    );
  }
}