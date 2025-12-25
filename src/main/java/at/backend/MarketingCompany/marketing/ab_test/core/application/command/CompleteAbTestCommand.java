package at.backend.MarketingCompany.marketing.ab_test.core.application.command;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;

import java.math.BigDecimal;

public record CompleteAbTestCommand(
    AbTestId testId,
    String winningVariant,
    BigDecimal statisticalSignificance
) {}