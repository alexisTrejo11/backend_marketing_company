package at.backend.MarketingCompany.marketing.activity.core.application.command;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;

import java.time.LocalDateTime;

public record RecordActivityDatesCommand(
    CampaignActivityId activityId,
    LocalDateTime actualStartDate,
    LocalDateTime actualEndDate
) {}