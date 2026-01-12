package at.backend.MarketingCompany.marketing.interaction.core.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.customer.core.port.ouput.CustomerCompanyRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.MarkAsConversionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.RemoveInteractionPropertyCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.RevertConversionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.TrackInteractionCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateConversionValueCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateDeviceInfoCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateInteractionPropertyCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateLocationInfoCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdatePageInfoCommand;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.UpdateUTMParametersCommand;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CreateInteractionParams;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.port.input.InteractionCommandInputPort;
import at.backend.MarketingCompany.marketing.interaction.core.port.output.InteractionRepositoryPort;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignInteractionService implements InteractionCommandInputPort {

  private final InteractionRepositoryPort interactionRepository;
  private final CampaignRepositoryPort campaignRepository;
  private final CustomerCompanyRepositoryPort customerRepository;

  @Override
  @Transactional
  public CampaignInteraction trackInteraction(TrackInteractionCommand command) {
    log.debug("Tracking interaction for campaign: {}, customer: {}",
        command.campaignId().getValue(), command.customerId().getValue());

    if (!campaignRepository.existsById(command.campaignId())) {
      throw new BusinessRuleException("Campaign not found");
    }

    /*
     * TODO: Fix Customer Company dependency issue
     * CustomerCompany customer = customerRepository.findById(command.customerId())
     * .orElseThrow(() -> new BusinessRuleException("Customer not found"));
     *
     * if (customer.isBlocked()) {
     * throw new
     * BusinessRuleException("Cannot track interaction for blocked customer");
     * }
     */

    // value objects are created inside toCreateCommand
    CreateInteractionParams createParams = command.toCreateCommand();
    CampaignInteraction interaction = CampaignInteraction.create(createParams);

    CampaignInteraction savedInteraction = interactionRepository.save(interaction);
    log.info("Interaction tracked successfully: {}", savedInteraction.getId().getValue());

    return savedInteraction;
  }

  @Override
  @Transactional
  public CampaignInteraction updateConversionValue(UpdateConversionValueCommand command) {
    log.debug("Updating conversion value for interaction: {}, new value: {}",
        command.interactionId().getValue(),
        command.newValue());

    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());
    interaction.updateConversionValue(command.newValue(), command.reason());
    interaction.addProperty("value_updated_by", command.updatedBy());
    interaction.addProperty("value_update_reason_detail", command.reason());

    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("Conversion value updated: ID={}, Old={}, New={}",
        command.interactionId().getValue(),
        interaction.getConversionValue(),
        command.newValue());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public CampaignInteraction updateUTMParameters(UpdateUTMParametersCommand command) {
    log.debug("Updating UTM parameters for interaction: {}", command.interactionId().getValue());

    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());

    interaction.updateUTMParameters(
        command.source(),
        command.medium(),
        command.campaign(),
        command.content(),
        command.term());

    interaction.addProperty("utm_updated_by", command.updatedBy());
    interaction.addProperty("utm_updated_at", LocalDateTime.now().toString());

    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("UTM parameters updated: ID={}", command.interactionId().getValue());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public CampaignInteraction updateDeviceInfo(UpdateDeviceInfoCommand command) {
    log.debug("Updating device info for interaction: {}", command.interactionId().getValue());

    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());

    interaction.updateDeviceInfo(
        command.deviceType(),
        command.deviceOs(),
        command.browser());

    interaction.addProperty("device_info_updated_by", command.updatedBy());

    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("Device info updated: ID={}", command.interactionId().getValue());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public CampaignInteraction updateLocationInfo(UpdateLocationInfoCommand command) {
    log.debug("Updating location info for interaction: {}", command.interactionId().getValue());

    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());

    interaction.updateLocationInfo(
        command.countryCode(),
        command.city());

    interaction.addProperty("location_updated_by", command.updatedBy());

    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("Location info updated: ID={}", command.interactionId().getValue());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public CampaignInteraction updatePageInfo(UpdatePageInfoCommand command) {
    log.debug("Updating page info for interaction: {}", command.interactionId().getValue());

    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());

    interaction.updatePageInfo(
        command.landingPageUrl(),
        command.referrerUrl());

    interaction.addProperty("page_info_updated_by", command.updatedBy());

    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("Page info updated: ID={}", command.interactionId().getValue());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public CampaignInteraction removeProperty(RemoveInteractionPropertyCommand command) {
    log.debug("Removing property '{}' from interaction: {}",
        command.key(),
        command.interactionId().getValue());

    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());

    interaction.removeProperty(command.key());

    interaction.addProperty("property_removed_by", command.updatedBy());
    interaction.addProperty("property_removed", command.key());

    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("Property removed: Key={}, Interaction={}",
        command.key(),
        command.interactionId().getValue());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public void deleteInteraction(CampaignInteractionId interactionId) {
    log.debug("Deleting interaction: {}", interactionId.getValue());

    CampaignInteraction interaction = findInteractionByIdOrThrow(interactionId);

    if (interaction.isConversion()) {
      throw new BusinessRuleException(
          "Cannot delete interaction marked as conversion. "
              + "Please remove conversion status first.");
    }

    interaction.softDelete();
    interactionRepository.save(interaction);

    log.info("Interaction deleted successfully: {}", interactionId.getValue());
  }

  private CampaignInteraction findInteractionByIdOrThrow(CampaignInteractionId interactionId) {
    return interactionRepository.findById(interactionId)
        .orElseThrow(() -> new BusinessRuleException(
            "Interaction not found: " + interactionId.getValue()));
  }

  @Override
  @Transactional
  public CampaignInteraction markAsConversion(MarkAsConversionCommand command) {
    log.debug("Marking interaction as conversion: {}, deal: {}",
        command.interactionId().getValue(),
        command.dealId().getValue());

    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());
    interaction.markAsConversion(
        command.dealId(),
        command.conversionValue(),
        command.notes());

    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("Interaction marked as conversion: ID={}, Deal={}, Value={}",
        command.interactionId().getValue(),
        command.dealId().getValue(),
        command.conversionValue());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public CampaignInteraction revertConversion(RevertConversionCommand command) {
    log.debug("Reverting conversion for interaction: {}, reason: {}",
        command.interactionId().getValue(),
        command.reason());

    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());
    interaction.revertConversion();

    // Registry the reason and who reverted it
    interaction.addProperty("conversion_revert_reason", command.reason());
    interaction.addProperty("conversion_reverted_by", command.revertedBy());
    interaction.addProperty("conversion_reverted_at", LocalDateTime.now().toString());

    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("Conversion reverted: ID={}, By={}",
        command.interactionId().getValue(),
        command.revertedBy());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public CampaignInteraction updateProperty(UpdateInteractionPropertyCommand command) {
    log.debug("{} property '{}' for interaction: {}",
        command.overwrite() ? "Updating" : "Adding",
        command.key(),
        command.interactionId().getValue());

    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());

    if (command.overwrite()) {
      interaction.updateProperty(command.key(), command.value());
    } else {
      interaction.addProperty(command.key(), command.value());
    }

    interaction.addProperty("property_updated_by", command.updatedBy());

    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("Property {}: Key={}, Interaction={}",
        command.overwrite() ? "updated" : "added",
        command.key(),
        command.interactionId().getValue());

    return updatedInteraction;
  }

}
