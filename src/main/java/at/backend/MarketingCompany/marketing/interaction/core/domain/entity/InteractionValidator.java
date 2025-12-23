package at.backend.MarketingCompany.marketing.interaction.core.domain.entity;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InteractionValidator {

  public static void validateForTracking(
      LocalDateTime interactionDate,
      Object campaign,
      Object customer) {
    
    if (interactionDate != null && interactionDate.isAfter(LocalDateTime.now())) {
      throw new BusinessRuleException("Interaction date cannot be in the future");
    }
    
    if (campaign == null) {
      throw new BusinessRuleException("Campaign is required for interaction");
    }
    
    if (customer == null) {
      throw new BusinessRuleException("Customer is required for interaction");
    }
  }

  public static void validateConversionData(
      CampaignInteraction interaction,
      DealId dealId,
      BigDecimal conversionValue) {
    
    if (interaction == null) {
      throw new BusinessRuleException("Interaction cannot be null");
    }
    
    if (interaction.isConversion()) {
      throw new BusinessRuleException("Interaction is already marked as conversion");
    }
    
    if (dealId == null) {
      throw new BusinessRuleException("Deal ID is required for conversion");
    }
    
    if (conversionValue == null || conversionValue.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BusinessRuleException("Conversion value must be greater than zero");
    }
  }

  public static void validateCampaignIsActive(boolean isActive) {
    if (!isActive) {
      throw new BusinessRuleException("Cannot track interaction for inactive campaign");
    }
  }

  public static void validateCustomerIsNotBlocked(boolean isBlocked) {
    if (isBlocked) {
      throw new BusinessRuleException("Cannot track interaction for blocked customer");
    }
  }
}