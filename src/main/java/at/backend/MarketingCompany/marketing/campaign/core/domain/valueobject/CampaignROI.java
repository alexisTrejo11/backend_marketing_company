package at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject;

import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.InvalidBudgetException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object that represents Return on Investment (ROI) for a marketing campaign
 * ROI = ((Revenue - Cost) / Cost) * 100
 */
public record CampaignROI(
    BigDecimal totalRevenue,
    BigDecimal totalCost,
    BigDecimal roiPercentage,
    ROIStatus status
) {
  public CampaignROI {
    if (totalRevenue == null || totalRevenue.compareTo(BigDecimal.ZERO) < 0) {
      throw new InvalidBudgetException("Total revenue cannot be negative");
    }
    if (totalCost == null || totalCost.compareTo(BigDecimal.ZERO) < 0) {
      throw new InvalidBudgetException("Total cost cannot be negative");
    }
    if (roiPercentage == null) {
      throw new InvalidBudgetException("ROI percentage cannot be null");
    }
    if (status == null) {
      throw new InvalidBudgetException("ROI status cannot be null");
    }
  }

  /**
   * Creates a CampaignROI from revenue and cost
   * Calculates ROI percentage and determines status
   */
  public static CampaignROI from(BigDecimal totalRevenue, BigDecimal totalCost) {
    if (totalRevenue == null || totalRevenue.compareTo(BigDecimal.ZERO) < 0) {
      throw new InvalidBudgetException("Total revenue cannot be negative");
    }
    if (totalCost == null || totalCost.compareTo(BigDecimal.ZERO) < 0) {
      throw new InvalidBudgetException("Total cost cannot be negative");
    }

    BigDecimal roiPercentage = calculateROI(totalRevenue, totalCost);
    ROIStatus status = determineStatus(roiPercentage, totalCost);

    return new CampaignROI(totalRevenue, totalCost, roiPercentage, status);
  }

  /**
   * Calculates ROI percentage
   * Formula: ((Revenue - Cost) / Cost) * 100
   */
  private static BigDecimal calculateROI(BigDecimal revenue, BigDecimal cost) {
    // If cost is zero, ROI cannot be calculated
    if (cost.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }

    BigDecimal profit = revenue.subtract(cost);
    BigDecimal roi = profit
        .divide(cost, 4, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));

    return roi.setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Determines ROI status based on percentage and investment
   */
  private static ROIStatus determineStatus(BigDecimal roiPercentage, BigDecimal cost) {
    // No investment yet
    if (cost.compareTo(BigDecimal.ZERO) == 0) {
      return ROIStatus.NOT_AVAILABLE;
    }

    // Excellent ROI (> 500%)
    if (roiPercentage.compareTo(BigDecimal.valueOf(500)) > 0) {
      return ROIStatus.EXCELLENT;
    }

    // Very Good ROI (200% - 500%)
    if (roiPercentage.compareTo(BigDecimal.valueOf(200)) > 0) {
      return ROIStatus.VERY_GOOD;
    }

    // Good ROI (100% - 200%)
    if (roiPercentage.compareTo(BigDecimal.valueOf(100)) > 0) {
      return ROIStatus.GOOD;
    }

    // Positive ROI (0% - 100%)
    if (roiPercentage.compareTo(BigDecimal.ZERO) > 0) {
      return ROIStatus.POSITIVE;
    }

    // Break Even (exactly 0%)
    if (roiPercentage.compareTo(BigDecimal.ZERO) == 0) {
      return ROIStatus.BREAK_EVEN;
    }

    // Negative ROI (< 0%)
    return ROIStatus.NEGATIVE;
  }

  /**
   * Returns the net profit/loss
   */
  public BigDecimal netProfit() {
    return totalRevenue.subtract(totalCost);
  }

  /**
   * Returns true if the campaign is profitable (ROI > 0)
   */
  public boolean isProfitable() {
    return roiPercentage.compareTo(BigDecimal.ZERO) > 0;
  }

  /**
   * Returns true if the campaign has broken even (ROI = 0)
   */
  public boolean isBreakEven() {
    return roiPercentage.compareTo(BigDecimal.ZERO) == 0;
  }

  /**
   * Returns true if the campaign is losing money (ROI < 0)
   */
  public boolean isLosing() {
    return roiPercentage.compareTo(BigDecimal.ZERO) < 0;
  }

  /**
   * Returns true if ROI meets the minimum acceptable threshold
   */
  public boolean meetsThreshold(BigDecimal minAcceptableROI) {
    if (minAcceptableROI == null) {
      return true;
    }
    return roiPercentage.compareTo(minAcceptableROI) >= 0;
  }

  /**
   * Returns the additional revenue needed to reach target ROI
   */
  public BigDecimal revenueNeededForTargetROI(BigDecimal targetROIPercentage) {
    if (targetROIPercentage == null || targetROIPercentage.compareTo(BigDecimal.ZERO) < 0) {
      throw new InvalidBudgetException("Target ROI percentage must be non-negative");
    }

    if (totalCost.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }

    // Target Revenue = Cost * (1 + TargetROI/100)
    BigDecimal targetRevenue = totalCost.multiply(
        BigDecimal.ONE.add(targetROIPercentage.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP))
    );

    BigDecimal additionalRevenue = targetRevenue.subtract(totalRevenue);
    return additionalRevenue.max(BigDecimal.ZERO);
  }

  /**
   * Enum representing ROI status categories
   */
  public enum ROIStatus {
    EXCELLENT("Excellent - Outstanding ROI (> 500%)"),
    VERY_GOOD("Very Good - Strong ROI (200-500%)"),
    GOOD("Good - Healthy ROI (100-200%)"),
    POSITIVE("Positive - Profitable (0-100%)"),
    BREAK_EVEN("Break Even - No profit or loss"),
    NEGATIVE("Negative - Losing money"),
    NOT_AVAILABLE("Not Available - No investment yet");

    private final String description;

    ROIStatus(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }
  }
}