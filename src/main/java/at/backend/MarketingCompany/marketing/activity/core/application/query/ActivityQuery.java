package at.backend.MarketingCompany.marketing.activity.core.application.query;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;

import java.time.LocalDateTime;
import java.util.List;

public record ActivityQuery(
    Long campaignId,
    List<ActivityStatus> statuses,
    List<ActivityType> activityTypes,
    Long assignedToUserId,
    LocalDateTime plannedStartFrom,
    LocalDateTime plannedStartTo,
    Boolean isDelayed,
    Boolean isCompleted,
    String searchTerm
) {
  public static ActivityQuery empty() {
    return new ActivityQuery(
        null, null, null, null, null, null, null, null, null
    );
  }

  public boolean isEmpty() {
    return campaignId == null && 
           (statuses == null || statuses.isEmpty()) &&
           (activityTypes == null || activityTypes.isEmpty()) &&
           assignedToUserId == null &&
           plannedStartFrom == null &&
           plannedStartTo == null &&
           isDelayed == null &&
           isCompleted == null &&
           (searchTerm == null || searchTerm.isBlank());
  }
}