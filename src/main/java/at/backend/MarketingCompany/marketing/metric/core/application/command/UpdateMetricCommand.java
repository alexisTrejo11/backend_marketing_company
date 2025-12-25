package at.backend.MarketingCompany.marketing.metric.core.application.command;

import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;

import java.math.BigDecimal;

public record UpdateMetricCommand(
    CampaignMetricId metricId,
    String description,
    BigDecimal targetValue,
    String calculationFormula,
    String dataSource
) {}
