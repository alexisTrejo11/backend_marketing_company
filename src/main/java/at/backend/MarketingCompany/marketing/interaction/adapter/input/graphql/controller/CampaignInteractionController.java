package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.controller;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.output.*;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.InteractionStatistics;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import org.springframework.data.domain.Page;

import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.mapper.MarketingInteractionOutputMapper;
import at.backend.MarketingCompany.marketing.interaction.core.port.input.InteractionQueryInputPort;
import at.backend.MarketingCompany.marketing.interaction.core.port.input.InteractionStatisticsInputPort;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CampaignInteractionController {
  private final InteractionStatisticsInputPort statsService;
  private final InteractionQueryInputPort interactionService;
  private final MarketingInteractionOutputMapper interactionMapper;

  @QueryMapping
  public MarketingInteractionOutput marketingInteraction(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Query: interaction with id: {}", id);

    var interactionId = new CampaignInteractionId(id);
    CampaignInteraction interaction = interactionService.getInteractionById(interactionId);

    return interactionMapper.toResponse(interaction);
  }

  @QueryMapping
  public PageResponse<MarketingInteractionOutput> marketingInteractionsByCampaign(
      @Argument @NotNull @Positive Long campaignId,
      @Argument PageInput pageInput) {

    log.debug("GraphQL Query: interactionsByCampaign with campaignId: {}", campaignId);

    Page<CampaignInteraction> interactions = interactionService.getInteractionsByCampaign(
        new MarketingCampaignId(campaignId), pageInput.toPageable());

    return interactionMapper.toPageResponse(interactions);
  }

  @QueryMapping
  public PageResponse<MarketingInteractionOutput> marketingInteractionsByCustomer(
      @Argument @NotNull @Positive Long customerId,
      @Argument PageInput pageInput) {

    log.debug("GraphQL Query: interactionsByCustomer with customerId: {}", customerId);

    Page<CampaignInteraction> interactions = interactionService.getInteractionsByCustomer(
        new CustomerCompanyId(customerId), pageInput.toPageable());

    return interactionMapper.toPageResponse(interactions);
  }

  @QueryMapping
  public PageResponse<MarketingInteractionOutput> conversionsByCampaign(
      @Argument @NotNull @Positive Long campaignId,
      @Argument PageInput pageInput) {

    log.debug("GraphQL Query: conversionsByCampaign with campaignId: {}", campaignId);

    Page<CampaignInteraction> conversions = interactionService.getConversionsByCampaign(
        new MarketingCampaignId(campaignId), pageInput.toPageable());

    return interactionMapper.toPageResponse(conversions);
  }

  @QueryMapping
  public InteractionStatisticsOutput marketingInteractionStatistics(
      @Argument @NotNull @Positive Long campaignId) {

    log.debug("GraphQL Query: interactionStatistics for campaign: {}", campaignId);

    InteractionStatistics stats = statsService.getInteractionStatistics(
        new MarketingCampaignId(campaignId));

    return InteractionStatisticsOutput.from(stats);
  }

}
