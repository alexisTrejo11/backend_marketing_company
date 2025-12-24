package at.backend.MarketingCompany.marketing.activity.core.application.dto;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ActivityStatistics(
    Long campaignId,
    String campaignName,
    Long totalActivities,
    Long plannedActivities,
    Long inProgressActivities,
    Long completedActivities,
    Long cancelledActivities,
    Long blockedActivities,
    Long delayedActivities,
    BigDecimal totalPlannedCost,
    BigDecimal totalActualCost,
    BigDecimal averageCostOverrun,
    Double completionRate,
    Double onTimeCompletionRate,
    ActivityTypeBreakdown typeBreakdown,
    ActivityTimeMetrics timeMetrics
) {
  @Builder
  public record ActivityTypeBreakdown(
      Long contentCreation,
      Long adSetup,
      Long emailBlast,
      Long socialPost,
      Long event,
      Long webinar,
      Long analysis,
      Long optimization
  ) {}

  @Builder
  public record ActivityTimeMetrics(
      Long averageCompletionTimeHours,
      Long totalDelayHours,
      Long activitiesCompletedEarly,
      Long activitiesCompletedLate,
      Long activitiesCompletedOnTime
  ) {}
}
