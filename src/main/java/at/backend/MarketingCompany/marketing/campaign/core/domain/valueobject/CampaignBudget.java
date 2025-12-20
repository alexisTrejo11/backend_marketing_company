package at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject;

import java.math.BigDecimal;

public record CampaignBudget(BigDecimal totalBudget, BigDecimal spentAmount) {
  public CampaignBudget {
    if (totalBudget == null || totalBudget.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Total budget must be positive");
    }
    if (spentAmount == null) {
      spentAmount = BigDecimal.ZERO;
    }
    if (spentAmount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Spent amount cannot be negative");
    }
    if (spentAmount.compareTo(totalBudget) > 0) {
      throw new IllegalArgumentException("Spent amount cannot exceed total budget");
    }
  }

  public BigDecimal remainingBudget() {
    return totalBudget.subtract(spentAmount);
  }

  public BigDecimal utilizationPercentage() {
    if (totalBudget.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return spentAmount.divide(totalBudget, 4, BigDecimal.ROUND_HALF_UP)
        .multiply(BigDecimal.valueOf(100));
  }

  public boolean isOverBudget() {
    return spentAmount.compareTo(totalBudget) > 0;
  }

  public boolean isNearlyExhausted() {
    return utilizationPercentage().compareTo(BigDecimal.valueOf(90)) >= 0;
  }

  public CampaignBudget addSpending(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Spending amount must be positive");
    }
    return new CampaignBudget(totalBudget, spentAmount.add(amount));
  }
}