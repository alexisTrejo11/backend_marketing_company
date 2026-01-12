package at.backend.MarketingCompany.marketing.activity.core.application.command;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;

import java.math.BigDecimal;

public record UpdateActivityCostCommand(
    CampaignActivityId activityId,
    BigDecimal actualCost,
    String reason
) {}