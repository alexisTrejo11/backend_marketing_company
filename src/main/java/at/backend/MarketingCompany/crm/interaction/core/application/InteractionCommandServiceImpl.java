package at.backend.MarketingCompany.crm.interaction.core.application;

import at.backend.MarketingCompany.crm.deal.core.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.interaction.core.application.commands.*;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.interaction.core.domain.exceptions.InteractionNotFoundException;
import at.backend.MarketingCompany.crm.interaction.core.ports.input.InteractionCommandService;
import at.backend.MarketingCompany.crm.interaction.core.ports.output.InteractionRepository;
import at.backend.MarketingCompany.shared.domain.exceptions.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InteractionCommandServiceImpl implements InteractionCommandService {
  private final InteractionRepository interactionRepository;
  private final ExternalModuleValidator externalValidator;

  @Override
  @Transactional
  public Interaction createInteraction(CreateInteractionCommand command) {
    log.info("Creating interaction for customer: {}", command.customerCompanyId().value());

    validateExternalDependencies(command);
    log.info("External dependencies validated successfully for customer: {}", command.customerCompanyId().value());

    Interaction newInteraction = Interaction.create(command.toCreateParams());
    log.info("Interaction entity created successfully for customer: {}", command.customerCompanyId().value());

    Interaction savedInteraction = interactionRepository.save(newInteraction);
    log.info("Interaction created successfully with ID: {}", savedInteraction.getId().value());

    return savedInteraction;
  }

  @Override
  @Transactional
  public Interaction updateInteraction(UpdateInteractionCommand command) {
    log.info("Updating interaction: {}", command.interactionId());

    Interaction interaction = findInteractionOrThrow(InteractionId.from(command.interactionId()));

    interaction.updateDetails(
        command.type(),
        command.dateTime(),
        command.description(),
        command.outcome());

    Interaction updatedInteraction = interactionRepository.save(interaction);
    log.info("Interaction {} updated successfully", command.interactionId());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public Interaction addFeedback(AddFeedbackCommand command) {
    log.info("Adding feedback to interaction: {}", command.interactionId());

    Interaction interaction = findInteractionOrThrow(command.interactionId());
    interaction.addFeedback(command.feedbackType(), command.notes());

    Interaction updatedInteraction = interactionRepository.save(interaction);
    log.info("Feedback added to interaction {}", command.interactionId());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public Interaction updateChannelPreference(UpdateChannelPreferenceCommand command) {
    log.info("Updating channel preference for interaction: {}", command.interactionId());

    Interaction interaction = findInteractionOrThrow(command.interactionId());
    interaction.updateChannelPreference(command.channelPreference());

    Interaction updatedInteraction = interactionRepository.save(interaction);
    log.info("Channel preference updated for interaction {}", command.interactionId());

    return updatedInteraction;
  }

  @Override
  @Transactional
  public Interaction markAsPositiveFeedback(MarkPositiveFeedbackCommand command) {
    return changeFeedbackType(command.interactionId(), "positive", Interaction::markAsPositiveFeedback);
  }

  @Override
  @Transactional
  public Interaction markAsNegativeFeedback(MarkNegativeFeedbackCommand command) {
    return changeFeedbackType(command.interactionId(), "negative", Interaction::markAsNegativeFeedback);
  }

  @Override
  @Transactional
  public Interaction markAsNeutralFeedback(MarkNeutralFeedbackCommand command) {
    return changeFeedbackType(command.interactionId(), "neutral", Interaction::markAsNeutralFeedback);
  }

  @Override
  @Transactional
  public void deleteInteraction(DeleteInteractionCommand command) {
    log.info("Deleting interaction: {}", command.interactionId());

    Interaction interaction = findInteractionOrThrow(command.interactionId());
    interaction.softDelete();

    interactionRepository.delete(interaction);
    log.info("Interaction {} deleted successfully", command.interactionId());
  }

  private Interaction findInteractionOrThrow(InteractionId interactionId) {
    return interactionRepository.findById(interactionId)
        .orElseThrow(() -> new InteractionNotFoundException(interactionId));
  }

  private Interaction changeFeedbackType(
      InteractionId interactionId,
      String feedbackType,
      FeedbackTypeChanger feedbackChanger) {
    log.info("Marking interaction {} as {} feedback", interactionId, feedbackType);

    Interaction interaction = findInteractionOrThrow(interactionId);
    feedbackChanger.changeFeedback(interaction);

    Interaction updatedInteraction = interactionRepository.save(interaction);
    log.info("Interaction {} marked as {} feedback", interactionId, feedbackType);

    return updatedInteraction;
  }

  private void validateExternalDependencies(CreateInteractionCommand command) {
    try {
      externalValidator.validateCustomerExists(command.customerCompanyId());
    } catch (ExternalServiceException e) {
      log.error("External validation failed for interaction creation: {}", e.getMessage());
      throw e;
    }
  }

  @FunctionalInterface
  private interface FeedbackTypeChanger {
    void changeFeedback(Interaction interaction);
  }
}
