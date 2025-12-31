package at.backend.MarketingCompany.marketing.activity.core.domain.valueobject;

import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record ActivitySchedule(
    LocalDateTime plannedStartDate,
    LocalDateTime plannedEndDate,
    LocalDateTime actualStartDate,
    LocalDateTime actualEndDate
) {
  public ActivitySchedule {
    if (plannedStartDate == null) {
      throw new MarketingDomainException("Planned start date is required");
    }
    if (plannedEndDate == null) {
      throw new MarketingDomainException("Planned end date is required");
    }
    if (plannedEndDate.isBefore(plannedStartDate)) {
      throw new MarketingDomainException("Planned end date must be after start date");
    }
    if (actualStartDate != null && actualEndDate != null && actualEndDate.isBefore(actualStartDate)) {
      throw new MarketingDomainException("Actual end date must be after actual start date");
    }
  }

  public ActivitySchedule recordWithActualDates(
      LocalDateTime actualStartDate,
      LocalDateTime actualEndDate
  ) {
    return new ActivitySchedule(
        this.plannedStartDate,
        this.plannedEndDate,
        actualStartDate,
        actualEndDate
    );
  }

  public static ActivitySchedule createPlannedSchedule(
      LocalDateTime plannedStartDate,
      LocalDateTime plannedEndDate
  ) {
    return new ActivitySchedule(plannedStartDate, plannedEndDate, null, null);
  }

  public boolean isCompleted() {
    return actualStartDate != null && actualEndDate != null;
  }

  public boolean isInProgress() {
    return actualStartDate != null && actualEndDate == null;
  }

  public boolean isDelayed() {
    if (actualEndDate != null) {
      return actualEndDate.isAfter(plannedEndDate);
    }
    return LocalDateTime.now().isAfter(plannedEndDate);
  }

  public long plannedDurationHours() {
    return ChronoUnit.HOURS.between(plannedStartDate, plannedEndDate);
  }

  public long actualDurationHours() {
    if (actualStartDate == null) {
      return 0;
    }
    LocalDateTime endTime = actualEndDate != null ? actualEndDate : LocalDateTime.now();
    return ChronoUnit.HOURS.between(actualStartDate, endTime);
  }
}
