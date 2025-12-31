package at.backend.MarketingCompany.marketing.ab_test.core.domain;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record AbTestUpdateParams(
    String hypothesis,
    BigDecimal confidenceLevel,
    Integer requiredSampleSize,
    LocalDateTime endDate,
    TestType testType,
    LocalDateTime startDate // Only for validation purposes
) {}