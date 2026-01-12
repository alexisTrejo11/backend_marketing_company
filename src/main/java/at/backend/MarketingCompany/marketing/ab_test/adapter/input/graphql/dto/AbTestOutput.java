package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import com.fasterxml.jackson.databind.JsonNode;
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
    JsonNode treatmentVariants,
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
