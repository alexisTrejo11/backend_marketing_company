package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.mapper.MarketingOutputMapper;
import at.backend.MarketingCompany.marketing.campaign.core.application.query.CampaignQuery;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.input.CampaignCommandServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.ports.input.CampaignQueryServicePort;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;

// TODO: Add Statistics Queries
@Slf4j
@Controller
@RequiredArgsConstructor
public class MarketingCampaignQueryController {
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
  public CampaignOutput campaign(@Argument @Valid @NotBlank String id) {
    log.debug("GraphQL Query: campaign with id: {}", id);

	  var campaignId = MarketingCampaignId.of(id);
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