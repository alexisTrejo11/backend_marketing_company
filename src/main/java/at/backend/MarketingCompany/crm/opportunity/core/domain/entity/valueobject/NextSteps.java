package at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject;

import java.time.LocalDateTime;

import at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions.OpportunityValidationException;

public record NextSteps(String value, LocalDateTime dueDate) {

  public static final NextSteps EMPTY = new NextSteps("", null);

  public NextSteps {
    validate(value, dueDate);
  }

  private static void validate(String value, LocalDateTime dueDate) {
    if (value == null) {
      throw new OpportunityValidationException("Next steps value cannot be null");
    }
    if (value.length() > 1000) {
      throw new OpportunityValidationException("Next steps cannot exceed 1000 characters");
    }
    if (dueDate != null && dueDate.isBefore(LocalDateTime.now())) {
      throw new OpportunityValidationException("Due date cannot be in the past");
    }
  }

  public static NextSteps createInitial() {
    return new NextSteps("Initial contact and needs assessment",
        LocalDateTime.now().plusDays(7));
  }

  public static NextSteps withCloseNotes(String notes) {
    return new NextSteps("Closed: " + notes, null);
  }

  public static NextSteps withReopenReason(String reason) {
    return new NextSteps("Reopened: " + reason,
        LocalDateTime.now().plusDays(3));
  }

  public static NextSteps create(String value, LocalDateTime dueDate) {
    return new NextSteps(value, dueDate);
  }

  public static NextSteps create(String value, int daysFromNow) {
    LocalDateTime dueDate = LocalDateTime.now().plusDays(daysFromNow);
    return new NextSteps(value, dueDate);
  }

  public boolean hasDueDate() {
    return dueDate != null;
  }

  public boolean isOverdue() {
    if (dueDate == null)
      return false;
    return dueDate.isBefore(LocalDateTime.now());
  }

  public boolean isEmpty() {
    return value.isBlank() && dueDate == null;
  }

  @Override
  public String toString() {
    return value + (hasDueDate() ? " [Due: " + dueDate.toLocalDate() + "]" : "");
  }
}
