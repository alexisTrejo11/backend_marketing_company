package at.backend.MarketingCompany.crm.interaction.application;

import at.backend.MarketingCompany.crm.deal.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.interaction.application.commands.*;
import at.backend.MarketingCompany.crm.interaction.application.queries.*;
import at.backend.MarketingCompany.crm.interaction.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.CreateInteractionParams;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import at.backend.MarketingCompany.crm.interaction.domain.exceptions.InteractionNotFoundException;
import at.backend.MarketingCompany.common.exceptions.ExternalServiceException;
import at.backend.MarketingCompany.crm.interaction.domain.repository.InteractionRepository;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InteractionApplicationService {

  private final InteractionRepository interactionRepository;
  private final ExternalModuleValidator externalValidator;

  @Transactional
  public Interaction handle(CreateInteractionCommand command) {
    log.info("Creating interaction for customer: {}", command.customerId().value());

    validateExternalDependencies(command);

    var createParams = CreateInteractionParams.builder()
        .customerId(command.customerId())
        .type(command.type())
        .dateTime(command.dateTime())
        .description(command.description())
        .outcome(command.outcome())
        .feedbackType(command.feedbackType())
        .channelPreference(command.channelPreference())
        .build();

    Interaction newInteraction = Interaction.create(createParams);
    Interaction savedInteraction = interactionRepository.save(newInteraction);

    log.info("Interaction created successfully with ID: {}", savedInteraction.getId().value());
    return savedInteraction;
  }

  @Transactional
  public Interaction handle(UpdateInteractionCommand command) {
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

  @Transactional
  public Interaction handle(AddFeedbackCommand command) {
    log.info("Adding feedback to interaction: {}", command.interactionId());

    Interaction interaction = findInteractionOrThrow(command.interactionId());
    interaction.addFeedback(command.feedbackType(), command.notes());

    Interaction updatedInteraction = interactionRepository.save(interaction);
    log.info("Feedback added to interaction {}", command.interactionId());

    return updatedInteraction;
  }

  @Transactional
  public Interaction handle(UpdateChannelPreferenceCommand command) {
    log.info("Updating channel preference for interaction: {}", command.interactionId());

    Interaction interaction = findInteractionOrThrow(command.interactionId());
    interaction.updateChannelPreference(command.channelPreference());

    Interaction updatedInteraction = interactionRepository.save(interaction);
    log.info("Channel preference updated for interaction {}", command.interactionId());

    return updatedInteraction;
  }

  @Transactional
  public Interaction handle(MarkPositiveFeedbackCommand command) {
    return changeFeedbackType(command.interactionId(), "positive", Interaction::markAsPositiveFeedback);
  }

  @Transactional
  public Interaction handle(MarkNegativeFeedbackCommand command) {
    return changeFeedbackType(command.interactionId(), "negative", Interaction::markAsNegativeFeedback);
  }

  @Transactional
  public Interaction handle(MarkNeutralFeedbackCommand command) {
    return changeFeedbackType(command.interactionId(), "neutral", Interaction::markAsNeutralFeedback);
  }

  @Transactional
  public void handle(DeleteInteractionCommand command) {
    log.info("Deleting interaction: {}", command.interactionId());

    Interaction interaction = findInteractionOrThrow(command.interactionId());
    interaction.markAsDeleted();

    interactionRepository.delete(interaction);
    log.info("Interaction {} deleted successfully", command.interactionId());
  }

  // ===== QUERY HANDLERS =====

  @Transactional(readOnly = true)
  public Interaction handle(GetInteractionByIdQuery query) {
    log.debug("Fetching interaction by ID: {}", query.interactionId());

    return interactionRepository.findById(query.interactionId())
        .orElseThrow(() -> new InteractionNotFoundException(query.interactionId()));
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetInteractionsByCustomerQuery query) {
    log.debug("Fetching interactions for customer: {}", query.customerId());

    return interactionRepository.findByCustomer(query.customerId(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetInteractionsByTypeQuery query) {
    log.debug("Fetching interactions by type: {}", query.type());

    return interactionRepository.findByType(query.type(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetInteractionsByFeedbackTypeQuery query) {
    log.debug("Fetching interactions by feedback type: {}", query.feedbackType());

    return interactionRepository.findByFeedbackType(query.feedbackType(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetInteractionsByCustomerAndTypeQuery query) {
    log.debug("Fetching interactions for customer {} and type: {}", query.customerId(), query.type());

    return interactionRepository.findByCustomerAndType(query.customerId(), query.type(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetInteractionsByDateRangeQuery query) {
    log.debug("Fetching interactions between {} and {}", query.startDate(), query.endDate());

    return interactionRepository.findByDateRange(query.startDate(), query.endDate(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetRecentInteractionsQuery query) {
    log.debug("Fetching recent interactions from last {} days", query.days());

    return interactionRepository.findRecentInteractions(query.days(), query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetTodayInteractionsQuery query) {
    log.debug("Fetching today's interactions");

    return interactionRepository.findTodayInteractions(query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetInteractionsRequiringFollowUpQuery query) {
    log.debug("Fetching interactions requiring follow-up");

    return interactionRepository.findInteractionsRequiringFollowUp(query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetPositiveInteractionsQuery query) {
    log.debug("Fetching positive interactions");

    return interactionRepository.findPositiveInteractions(query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<Interaction> handle(GetNegativeInteractionsQuery query) {
    log.debug("Fetching negative interactions");

    return interactionRepository.findNegativeInteractions(query.pageable());
  }

  @Transactional(readOnly = true)
  public List<Interaction> handle(SearchInteractionsQuery query) {
    log.debug("Searching interactions with criteria: {}", query);

    // TODO: Implement search logic based on criteria in query
    return null;
  }

  @Transactional(readOnly = true)
  public InteractionStatistics handle(GetInteractionStatisticsQuery query) {
    log.debug("Fetching interaction statistics for customer: {}", query.customerId());

    long totalInteractions = interactionRepository.findByCustomer(query.customerId()).size();
    long recentInteractions = interactionRepository.countRecentInteractionsByCustomer(query.customerId(), 30);
    long positiveInteractions = interactionRepository.countByCustomerAndFeedbackType(query.customerId(),
        FeedbackType.POSITIVE);
    long negativeInteractions = interactionRepository.countByCustomerAndFeedbackType(query.customerId(),
        FeedbackType.NEGATIVE);
    long followUpRequired = interactionRepository.findInteractionsRequiringFollowUp().stream()
        .filter(interaction -> interaction.getCustomerId().equals(query.customerId()))
        .count();

    return new InteractionStatistics(
        totalInteractions,
        recentInteractions,
        positiveInteractions,
        negativeInteractions,
        followUpRequired);
  }

  @Transactional(readOnly = true)
  public CustomerInteractionAnalytics handle(GetCustomerInteractionAnalyticsQuery query) {
    log.debug("Fetching customer interaction analytics for: {}", query.customerId());

    CustomerId customerId = new CustomerId(query.customerId());

    List<InteractionType> frequentTypes = interactionRepository.findMostFrequentInteractionTypesByCustomer(customerId);
    FeedbackType predominantFeedback = interactionRepository.findPredominantFeedbackByCustomer(customerId);
    long totalInteractions = interactionRepository.findByCustomer(customerId).size();

    // Calculate interaction frequency (interactions per month)
    double monthlyFrequency = calculateMonthlyFrequency(customerId);

    return new CustomerInteractionAnalytics(
        customerId.value(),
        frequentTypes,
        predominantFeedback,
        totalInteractions,
        monthlyFrequency);
  }

  private Interaction findInteractionOrThrow(InteractionId interactionId) {
    return interactionRepository.findById(interactionId)
        .orElseThrow(() -> new InteractionNotFoundException(interactionId));
  }

  private Interaction changeFeedbackType(InteractionId interactionId, String feedbackType,
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
      externalValidator.validateCustomerExists(command.customerId());
    } catch (ExternalServiceException e) {
      log.error("External validation failed for interaction creation: {}", e.getMessage());
      throw e;
    }
  }

  private double calculateMonthlyFrequency(CustomerId customerId) {
    List<Interaction> customerInteractions = interactionRepository.findByCustomer(customerId);
    if (customerInteractions.isEmpty()) {
      return 0.0;
    }

    // Find the earliest interaction date
    LocalDateTime earliestDate = customerInteractions.stream()
        .map(interaction -> interaction.getDateTime().value())
        .min(LocalDateTime::compareTo)
        .orElse(LocalDateTime.now());

    long monthsBetween = java.time.temporal.ChronoUnit.MONTHS.between(
        earliestDate.toLocalDate().withDayOfMonth(1),
        LocalDateTime.now().toLocalDate().withDayOfMonth(1));

    if (monthsBetween == 0) {
      monthsBetween = 1; // Avoid division by zero
    }

    return (double) customerInteractions.size() / monthsBetween;
  }

  @FunctionalInterface
  private interface FeedbackTypeChanger {
    void changeFeedback(Interaction interaction);
  }

  public record InteractionStatistics(
      long totalInteractions,
      long recentInteractions,
      long positiveInteractions,
      long negativeInteractions,
      long followUpRequired) {
  }

  public record CustomerInteractionAnalytics(
      String customerId,
      List<InteractionType> frequentInteractionTypes,
      FeedbackType predominantFeedback,
      long totalInteractions,
      double monthlyFrequency) {
  }
}
