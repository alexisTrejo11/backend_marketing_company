package at.backend.MarketingCompany.marketing.campaign.core.application.command;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

import java.math.BigDecimal;

public record AddCampaignSpendingCommand(
    MarketingCampaignId campaignId,
    BigDecimal amount,
    String description
) {}