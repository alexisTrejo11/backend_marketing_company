package at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record CampaignPeriod(LocalDate startDate, LocalDate endDate) {
  public CampaignPeriod {
    if (startDate == null) {
      throw new IllegalArgumentException("Start date is required");
    }
    if (endDate != null && endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("End date must be after start date");
    }
  }

  public boolean isActive() {
    LocalDate now = LocalDate.now();
    return !now.isBefore(startDate) && (endDate == null || !now.isAfter(endDate));
  }

  public boolean hasEnded() {
    return endDate != null && LocalDate.now().isAfter(endDate);
  }

  public boolean hasStarted() {
    return !LocalDate.now().isBefore(startDate);
  }

  public long durationInDays() {
    if (endDate == null) {
      return ChronoUnit.DAYS.between(startDate, LocalDate.now());
    }
    return ChronoUnit.DAYS.between(startDate, endDate);
  }
}