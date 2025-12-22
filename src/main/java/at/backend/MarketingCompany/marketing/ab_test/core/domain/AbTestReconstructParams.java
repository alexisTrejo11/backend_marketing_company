package at.backend.MarketingCompany.marketing.ab_test.core.domain;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record AbTestReconstructParams(
    AbTestId id,
    MarketingCampaignId campaignId,
    String testName,
    String hypothesis,
    TestType testType,
    String primaryMetric,
    BigDecimal confidenceLevel,
    Integer requiredSampleSize,
    String controlVariant,
    Map<String, Object> treatmentVariants,
    String winningVariant,
    BigDecimal statisticalSignificance,
    Boolean isCompleted,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version
) {}