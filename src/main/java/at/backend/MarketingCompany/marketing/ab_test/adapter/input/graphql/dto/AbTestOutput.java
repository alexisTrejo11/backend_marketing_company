package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record AbTestOutput(
    Long id,
    Long campaignId,
    String testName,
    String hypothesis,
    String testType,
    String primaryMetric,
    BigDecimal confidenceLevel,
    Integer requiredSampleSize,
    String controlVariant,
    Map<String, Object> treatmentVariants,
    String winningVariant,
    BigDecimal statisticalSignificance,
    Boolean isCompleted,
    Boolean hasStarted,
    Boolean hasEnded,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
