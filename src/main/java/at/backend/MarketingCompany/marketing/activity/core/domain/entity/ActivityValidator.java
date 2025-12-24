package at.backend.MarketingCompany.marketing.activity.core.domain.entity;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ActivityValidator {

  private static final int MIN_ADVANCE_HOURS = 1;

  public static void validateForCreation(
      LocalDateTime plannedStartDate,
      LocalDateTime plannedEndDate,
      BigDecimal plannedCost,
      Object campaign) {
    
    if (plannedStartDate == null) {
      throw new BusinessRuleException("Planned start date is required");
    }
    
    if (plannedEndDate == null) {
      throw new BusinessRuleException("Planned end date is required");
    }
    
    if (plannedStartDate.isBefore(LocalDateTime.now().plusHours(MIN_ADVANCE_HOURS))) {
      throw new BusinessRuleException(
          String.format("Activities must be planned at least %d hour(s) in advance", 
              MIN_ADVANCE_HOURS)
      );
    }
    
    if (plannedEndDate.isBefore(plannedStartDate)) {
      throw new BusinessRuleException("Planned end date must be after start date");
    }
    
    if (plannedCost == null || plannedCost.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BusinessRuleException("Planned cost must be greater than zero");
    }
    
    if (campaign == null) {
      throw new BusinessRuleException("Campaign is required for activity");
    }
  }

  public static void validateForUpdate(CampaignActivity activity) {
    if (activity == null) {
      throw new BusinessRuleException("Activity cannot be null");
    }
    
    if (activity.getStatus() == ActivityStatus.COMPLETED) {
      throw new BusinessRuleException("Cannot update a completed activity");
    }
    
    if (activity.getStatus() == ActivityStatus.CANCELLED) {
      throw new BusinessRuleException("Cannot update a cancelled activity");
    }
  }

  public static void validateCostUpdate(
      CampaignActivity activity,
      BigDecimal actualCost) {
    
    if (activity == null) {
      throw new BusinessRuleException("Activity cannot be null");
    }
    
    if (activity.getStatus() != ActivityStatus.IN_PROGRESS &&
        activity.getStatus() != ActivityStatus.COMPLETED) {
      throw new BusinessRuleException(
          "Can only update cost for in-progress or completed activities"
      );
    }
    
    if (actualCost == null || actualCost.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessRuleException("Actual cost cannot be negative");
    }
  }

  public static void validateForStart(CampaignActivity activity) {
    if (activity == null) {
      throw new BusinessRuleException("Activity cannot be null");
    }
    
    if (activity.getStatus() != ActivityStatus.PLANNED) {
      throw new BusinessRuleException("Only planned activities can be started");
    }
  }

  public static void validateForCompletion(CampaignActivity activity) {
    if (activity == null) {
      throw new BusinessRuleException("Activity cannot be null");
    }
    
    if (activity.getStatus() != ActivityStatus.IN_PROGRESS) {
      throw new BusinessRuleException("Only in-progress activities can be completed");
    }
  }

  public static void validateForCancellation(CampaignActivity activity) {
    if (activity == null) {
      throw new BusinessRuleException("Activity cannot be null");
    }
    
    if (activity.getStatus() == ActivityStatus.COMPLETED) {
      throw new BusinessRuleException("Cannot cancel a completed activity");
    }
    
    if (activity.getStatus() == ActivityStatus.CANCELLED) {
      throw new BusinessRuleException("Activity is already cancelled");
    }
  }
}