package at.backend.MarketingCompany.crm.interaction.core.ports.input;

import at.backend.MarketingCompany.crm.interaction.core.application.commands.*;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.Interaction;

public interface InteractionCommandService {

  Interaction createInteraction(CreateInteractionCommand command);

  Interaction updateInteraction(UpdateInteractionCommand command);

  Interaction addFeedback(AddFeedbackCommand command);

  Interaction updateChannelPreference(UpdateChannelPreferenceCommand command);

  Interaction markAsPositiveFeedback(MarkPositiveFeedbackCommand command);

  Interaction markAsNegativeFeedback(MarkNegativeFeedbackCommand command);

  Interaction markAsNeutralFeedback(MarkNeutralFeedbackCommand command);

  void deleteInteraction(DeleteInteractionCommand command);
}
