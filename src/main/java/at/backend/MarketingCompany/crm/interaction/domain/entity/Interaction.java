package at.backend.MarketingCompany.crm.interaction.domain.entity;

import at.backend.MarketingCompany.common.utils.BaseDomainEntity;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.interaction.domain.exceptions.InteractionValidationException;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class Interaction extends BaseDomainEntity<InteractionId> {
  private CustomerId customerId;
  private InteractionType type;
  private InteractionDateTime dateTime;
  private InteractionDescription description;
  private InteractionOutcome outcome;
  private FeedbackType feedbackType;
  private ChannelPreference channelPreference;

  private Interaction(InteractionId interactionId) {
    super(interactionId);
  }

  private Interaction(InteractionReconstructParams params) {
    super(params.id(), params.version(), params.deletedAt(), params.createdAt(), params.updatedAt());
    this.customerId = params.customerId();
    this.type = params.type();
    this.dateTime = params.dateTime();
    this.description = params.description();
    this.outcome = params.outcome();
    this.feedbackType = params.feedbackType();
    this.channelPreference = params.channelPreference();
    validateState();
  }

  // ===== FACTORY METHODS =====
  public static Interaction reconstruct(InteractionReconstructParams params) {
    return new Interaction(params);
  }

  public static Interaction create(CreateInteractionParams params) {
    validateCreationParams(params);

    Interaction newInteraction = new Interaction(InteractionId.create());
    newInteraction.customerId = params.customerId();
    newInteraction.type = params.type();
    newInteraction.dateTime = params.dateTime();
    newInteraction.description = params.description();
    newInteraction.outcome = params.outcome();
    newInteraction.feedbackType = params.feedbackType();
    newInteraction.channelPreference = params.channelPreference();
    newInteraction.createdAt = LocalDateTime.now();
    newInteraction.updatedAt = LocalDateTime.now();
    newInteraction.version = 1;

    return newInteraction;
  }

  // ===== BUSINESS LOGIC =====
  public void updateDetails(InteractionType type, InteractionDateTime dateTime,
      InteractionDescription description, InteractionOutcome outcome) {
    if (type == null) {
      throw new InteractionValidationException("Interaction type is required");
    }
    if (dateTime == null) {
      throw new InteractionValidationException("Interaction date time is required");
    }
    if (outcome == null) {
      throw new InteractionValidationException("Interaction outcome is required");
    }

    this.type = type;
    this.dateTime = dateTime;
    this.description = description;
    this.outcome = outcome;
    updateTimestamp();
  }

  public void addFeedback(FeedbackType feedbackType, String notes) {
    if (feedbackType == null) {
      throw new InteractionValidationException("Feedback type is required");
    }

    this.feedbackType = feedbackType;
    if (notes != null && !notes.trim().isEmpty()) {
      // Si ya tenemos una descripción, agregamos el feedback
      if (this.description != null) {
        String currentDescription = this.description.value();
        String updatedDescription = currentDescription + "\n\nFeedback: " + notes.trim();
        this.description = new InteractionDescription(updatedDescription);
      } else {
        this.description = new InteractionDescription("Feedback: " + notes.trim());
      }
    }
    updateTimestamp();
  }

  public void updateChannelPreference(ChannelPreference channelPreference) {
    this.channelPreference = channelPreference;
    updateTimestamp();
  }

  public void markAsPositiveFeedback() {
    this.feedbackType = FeedbackType.POSITIVE;
    updateTimestamp();
  }

  public void markAsNegativeFeedback() {
    this.feedbackType = FeedbackType.NEGATIVE;
    updateTimestamp();
  }

  public void markAsNeutralFeedback() {
    this.feedbackType = FeedbackType.NEUTRAL;
    updateTimestamp();
  }

  public boolean hasFeedback() {
    return feedbackType != null;
  }

  public boolean isPositiveFeedback() {
    return feedbackType == FeedbackType.POSITIVE;
  }

  public boolean isNegativeFeedback() {
    return feedbackType == FeedbackType.NEGATIVE;
  }

  public boolean isRecent() {
    return dateTime != null && dateTime.isRecent();
  }

  public boolean isFromLast7Days() {
    return dateTime != null && dateTime.isFromLastDays(7);
  }

  public boolean isFromLast30Days() {
    return dateTime != null && dateTime.isFromLastDays(30);
  }

  // ===== VALIDATIONS =====
  private void validateState() {
    if (customerId == null) {
      throw new InteractionValidationException("Customer ID is required");
    }
    if (type == null) {
      throw new InteractionValidationException("Interaction type is required");
    }
    if (dateTime == null) {
      throw new InteractionValidationException("Interaction date time is required");
    }
    if (outcome == null) {
      throw new InteractionValidationException("Interaction outcome is required");
    }
  }

  private static void validateCreationParams(CreateInteractionParams params) {
    if (params == null) {
      throw new InteractionValidationException("Creation parameters cannot be null");
    }
    if (params.customerId() == null) {
      throw new InteractionValidationException("Customer ID is required");
    }
    if (params.type() == null) {
      throw new InteractionValidationException("Interaction type is required");
    }
    if (params.dateTime() == null) {
      throw new InteractionValidationException("Interaction date time is required");
    }
    if (params.outcome() == null) {
      throw new InteractionValidationException("Interaction outcome is required");
    }
  }

  private void updateTimestamp() {
    this.updatedAt = LocalDateTime.now();
  }

  public Optional<InteractionDescription> getDescription() {
    return Optional.ofNullable(description);
  }

  public Optional<FeedbackType> getFeedbackType() {
    return Optional.ofNullable(feedbackType);
  }

  public Optional<ChannelPreference> getChannelPreference() {
    return Optional.ofNullable(channelPreference);
  }
}
