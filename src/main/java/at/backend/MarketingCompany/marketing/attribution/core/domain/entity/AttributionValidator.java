package at.backend.MarketingCompany.marketing.attribution.core.domain.entity;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AttributionValidator {

  public static void validateForCreation(
      DealId dealId,
      Object campaign,
      BigDecimal attributionPercentage,
      BigDecimal attributedRevenue) {
    
    if (dealId == null) {
      throw new BusinessRuleException("Deal ID is required for attribution");
    }
    
    if (campaign == null) {
      throw new BusinessRuleException("Campaign is required for attribution");
    }
    
    validateAttributionPercentage(attributionPercentage);
    validateAttributedRevenue(attributedRevenue);
  }

  public static void validateAttributionPercentage(BigDecimal percentage) {
    if (percentage == null) {
      throw new BusinessRuleException("Attribution percentage is required");
    }
    
    if (percentage.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessRuleException("Attribution percentage cannot be negative");
    }
    
    if (percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
      throw new BusinessRuleException("Attribution percentage cannot exceed 100%");
    }
  }

  public static void validateAttributedRevenue(BigDecimal revenue) {
    if (revenue == null) {
      throw new BusinessRuleException("Attributed revenue is required");
    }
    
    if (revenue.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessRuleException("Attributed revenue cannot be negative");
    }
  }

  public static void validateTouchpoint(LocalDateTime touchTimestamp) {
    if (touchTimestamp == null) {
      throw new BusinessRuleException("Touch timestamp is required");
    }
    
    if (touchTimestamp.isAfter(LocalDateTime.now())) {
      throw new BusinessRuleException("Touch timestamp cannot be in the future");
    }
  }

  public static void validateForUpdate(CampaignAttribution attribution) {
    if (attribution == null) {
      throw new BusinessRuleException("Attribution cannot be null");
    }
  }

  public static void validateWeights(
      BigDecimal firstTouchWeight,
      BigDecimal lastTouchWeight,
      BigDecimal linearWeight) {
    
    if (firstTouchWeight != null && firstTouchWeight.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessRuleException("First touch weight cannot be negative");
    }
    
    if (lastTouchWeight != null && lastTouchWeight.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessRuleException("Last touch weight cannot be negative");
    }
    
    if (linearWeight != null && linearWeight.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessRuleException("Linear weight cannot be negative");
    }
    
    // Validate total weights don't exceed 1.0 for custom model
    if (firstTouchWeight != null && lastTouchWeight != null && linearWeight != null) {
      BigDecimal totalWeight = firstTouchWeight.add(lastTouchWeight).add(linearWeight);
      if (totalWeight.compareTo(BigDecimal.ONE) > 0) {
        throw new BusinessRuleException("Total attribution weights cannot exceed 1.0");
      }
    }
  }
}
