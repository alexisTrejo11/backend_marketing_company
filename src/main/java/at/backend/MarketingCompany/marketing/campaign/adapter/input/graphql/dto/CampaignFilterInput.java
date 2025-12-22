package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignType;

import java.util.List;

public record CampaignFilterInput(
    List<CampaignType> campaignTypes,
    List<CampaignStatus> statuses,
    Long channelId,
    String startDateFrom,
    String startDateTo,
    String endDateFrom,
    String endDateTo,
    Double minBudget,
    Double maxBudget,
    Boolean isActive,
    String searchTerm
) {}