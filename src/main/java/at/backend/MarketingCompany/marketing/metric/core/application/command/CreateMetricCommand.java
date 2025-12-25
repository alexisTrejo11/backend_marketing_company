package at.backend.MarketingCompany.marketing.metric.core.application.command;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;

import java.math.BigDecimal;

public record CreateMetricCommand(
    MarketingCampaignId campaignId,
    String name,
    MetricType metricType,
    String description,
    BigDecimal targetValue,
    String measurementUnit,
    String calculationFormula,
    String dataSource,
    Boolean isAutomated
) {}