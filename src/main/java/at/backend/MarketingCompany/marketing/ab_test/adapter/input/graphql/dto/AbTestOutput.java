package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

@Builder
public record AbTestOutput(
    Long id,
    Long campaignId,
    String testName,
    String hypothesis,
    TestType testType,
    String primaryMetric,
    BigDecimal confidenceLevel,
    Integer requiredSampleSize,
    String controlVariant,
    String treatmentVariants, // Serialized as JSON string
    String winningVariant,
    BigDecimal statisticalSignificance,
    Boolean isCompleted,
    Boolean hasStarted,
    Boolean hasEnded,
    OffsetDateTime startDate,
    OffsetDateTime endDate,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
