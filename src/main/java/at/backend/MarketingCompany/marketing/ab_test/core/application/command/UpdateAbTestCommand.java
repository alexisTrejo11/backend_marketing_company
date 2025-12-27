package at.backend.MarketingCompany.marketing.ab_test.core.application.command;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateAbTestCommand(
    AbTestId testId,
    String hypothesis,
    BigDecimal confidenceLevel,
    Integer requiredSampleSize,
    LocalDateTime endDate,
    TestType testType
) {}