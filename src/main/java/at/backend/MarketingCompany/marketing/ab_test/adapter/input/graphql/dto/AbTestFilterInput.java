package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Filter input for searching A/B tests
 */
public record AbTestFilterInput(
    Long campaignId,
    List<TestType> testTypes,
    Boolean isCompleted,
    LocalDateTime startDateFrom, // ISO format
    LocalDateTime startDateTo, // ISO format
    BigDecimal minConfidenceLevel,
    String searchTerm
) {
    public AbTestQuery toQuery() {
        return new AbTestQuery(
            campaignId != null ? new MarketingCampaignId(campaignId) : null,
            testTypes,
            isCompleted,
            startDateFrom,
            startDateTo,
            minConfidenceLevel,
            searchTerm
        );
    }
}