package at.backend.MarketingCompany.marketing.interaction.core.port.input;

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
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

public interface InteractionCommandInputPort {

  CampaignInteraction trackInteraction(TrackInteractionCommand command);

  CampaignInteraction markAsConversion(MarkAsConversionCommand command);

  void deleteInteraction(CampaignInteractionId interactionId);

  CampaignInteraction revertConversion(RevertConversionCommand command);

  CampaignInteraction updateConversionValue(UpdateConversionValueCommand command);

  CampaignInteraction updateUTMParameters(UpdateUTMParametersCommand command);

  CampaignInteraction updateDeviceInfo(UpdateDeviceInfoCommand command);

  CampaignInteraction updateLocationInfo(UpdateLocationInfoCommand command);

  CampaignInteraction updatePageInfo(UpdatePageInfoCommand command);

  CampaignInteraction updateProperty(UpdateInteractionPropertyCommand command);

  CampaignInteraction removeProperty(RemoveInteractionPropertyCommand command);

}
