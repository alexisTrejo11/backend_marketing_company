package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input.*;
import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.output.*;
import at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.mapper.MarketingInteractionOutputMapper;
import at.backend.MarketingCompany.marketing.interaction.core.port.input.InteractionCommandInputPort;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.*;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CampaignInteractionMutationController {

  private final InteractionCommandInputPort commandService;
  private final MarketingInteractionOutputMapper interactionMapper;

  @MutationMapping
  public MarketingInteractionOutput trackMarketingInteraction(@Argument @Valid TrackInteractionInput input) {
    log.debug("GraphQL Mutation: trackInteraction for campaign: {}, customer: {}",
        input.campaignId(), input.customerId());

    TrackInteractionCommand command = input.toCommand();

    CampaignInteraction interaction = commandService.trackInteraction(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public MarketingInteractionOutput markAsConversion(@Argument @Valid MarkConversionInput input) {
    log.debug("GraphQL Mutation: markAsConversion for interaction: {}", input.interactionId());

    MarkAsConversionCommand command = input.toCommand();
    CampaignInteraction interaction = commandService.markAsConversion(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public MarketingInteractionOutput revertConversion(@Argument @Valid RevertConversionInput input) {
    log.debug("GraphQL Mutation: revertConversion for interaction: {}", input.interactionId());

    RevertConversionCommand command = input.toCommand();

    CampaignInteraction interaction = commandService.revertConversion(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public MarketingInteractionOutput updateConversionValue(@Argument @Valid UpdateConversionValueInput input) {
    log.debug("GraphQL Mutation: updateConversionValue for interaction: {}, new value: {}",
        input.interactionId(), input.newValue());

    UpdateConversionValueCommand command = new UpdateConversionValueCommand(
        new CampaignInteractionId(input.interactionId()),
        BigDecimal.valueOf(input.newValue()),
        input.reason(),
        input.updatedBy());

    CampaignInteraction interaction = commandService.updateConversionValue(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public MarketingInteractionOutput updateUTMParameters(@Argument @Valid UpdateUTMParametersInput input) {
    log.debug("GraphQL Mutation: updateUTMParameters for interaction: {}", input.interactionId());

    UpdateUTMParametersCommand command = input.toCommand();
    CampaignInteraction interaction = commandService.updateUTMParameters(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public MarketingInteractionOutput updateDeviceInfo(@Argument @Valid UpdateDeviceInfoInput input) {
    log.debug("GraphQL Mutation: updateDeviceInfo for interaction: {}", input.interactionId());

    UpdateDeviceInfoCommand command = input.toCommand();
    CampaignInteraction interaction = commandService.updateDeviceInfo(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public MarketingInteractionOutput updateLocationInfo(@Argument @Valid UpdateLocationInfoInput input) {
    log.debug("GraphQL Mutation: updateLocationInfo for interaction: {}", input.interactionId());

    UpdateLocationInfoCommand command = new UpdateLocationInfoCommand(
        new CampaignInteractionId(input.interactionId()),
        input.countryCode(),
        input.city(),
        input.updatedBy());

    CampaignInteraction interaction = commandService.updateLocationInfo(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public MarketingInteractionOutput updatePageInfo(@Argument @Valid UpdatePageInfoInput input) {
    log.debug("GraphQL Mutation: updatePageInfo for interaction: {}", input.interactionId());

    UpdatePageInfoCommand command = new UpdatePageInfoCommand(
        new CampaignInteractionId(input.interactionId()),
        input.landingPageUrl(),
        input.referrerUrl(),
        input.updatedBy());

    CampaignInteraction interaction = commandService.updatePageInfo(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public MarketingInteractionOutput updateInteractionProperty(@Argument @Valid UpdatePropertyInput input) {
    log.debug("GraphQL Mutation: updateProperty for interaction: {}, key: {}",
        input.interactionId(), input.key());

    UpdateInteractionPropertyCommand command = new UpdateInteractionPropertyCommand(
        new CampaignInteractionId(input.interactionId()),
        input.key(),
        input.value(),
        input.updatedBy(),
        input.overwrite());

    CampaignInteraction interaction = commandService.updateProperty(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public MarketingInteractionOutput removeInteractionProperty(@Argument @Valid RemovePropertyInput input) {
    log.debug("GraphQL Mutation: removeProperty for interaction: {}, key: {}",
        input.interactionId(), input.key());

    RemoveInteractionPropertyCommand command = new RemoveInteractionPropertyCommand(
        new CampaignInteractionId(input.interactionId()),
        input.key(),
        input.updatedBy());

    CampaignInteraction interaction = commandService.removeProperty(command);

    return interactionMapper.toResponse(interaction);
  }

  @MutationMapping
  public Boolean deleteMarketingInteraction(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: deleteInteraction with id: {}", id);

    commandService.deleteInteraction(new CampaignInteractionId(id));

    return true;
  }
}
