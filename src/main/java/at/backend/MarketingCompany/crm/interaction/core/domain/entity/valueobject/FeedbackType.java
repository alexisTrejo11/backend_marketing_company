package at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject;

import java.util.List;

public enum FeedbackType {
  POSITIVE("Customer expressed satisfaction"),
  NEGATIVE("Customer expressed dissatisfaction"),
  NEUTRAL("Customer provided neutral feedback"),
  SUGGESTION("Customer provided suggestions"),
  COMPLAINT("Customer registered a complaint"),
  COMPLIMENT("Customer provided compliments");

  private final String description;

  FeedbackType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public boolean isPositive() {
    return this == POSITIVE || this == COMPLIMENT;
  }

  public boolean isNegative() {
    return this == NEGATIVE || this == COMPLAINT;
  }

  public boolean requiresFollowUp() {
    return this == NEGATIVE || this == COMPLAINT || this == SUGGESTION;
  }

  public static List<FeedbackType> getPositiveFeedbackTypes() {
    return List.of(POSITIVE, COMPLIMENT);
  }
}
