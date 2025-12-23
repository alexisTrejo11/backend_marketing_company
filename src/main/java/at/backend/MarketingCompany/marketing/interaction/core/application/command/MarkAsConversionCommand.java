package at.backend.MarketingCompany.marketing.interaction.core.application.command;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;

import java.math.BigDecimal;

public record MarkAsConversionCommand(
    CampaignInteractionId interactionId,
    DealId dealId,
    BigDecimal conversionValue
) {}