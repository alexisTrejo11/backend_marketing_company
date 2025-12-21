package at.backend.MarketingCompany.marketing.activity.core.domain.valueobject;

import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.InvalidBudgetException;
import java.math.BigDecimal;

public record ActivityCost(BigDecimal plannedCost, BigDecimal actualCost) {
  public ActivityCost {
    if (plannedCost == null || plannedCost.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidBudgetException("Planned cost must be positive");
    }
    if (actualCost != null && actualCost.compareTo(BigDecimal.ZERO) < 0) {
      throw new InvalidBudgetException("Actual cost cannot be negative");
    }
  }

  public BigDecimal costVariance() {
    if (actualCost == null) {
      return BigDecimal.ZERO;
    }
    return actualCost.subtract(plannedCost);
  }

  public BigDecimal costOverrunPercentage() {
    if (actualCost == null || plannedCost.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return costVariance()
        .divide(plannedCost, 4, BigDecimal.ROUND_HALF_UP)
        .multiply(BigDecimal.valueOf(100));
  }

  public boolean isOverBudget() {
    return actualCost != null && actualCost.compareTo(plannedCost) > 0;
  }
}
