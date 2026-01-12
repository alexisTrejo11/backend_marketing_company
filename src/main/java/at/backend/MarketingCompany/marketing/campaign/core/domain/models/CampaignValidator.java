package at.backend.MarketingCompany.marketing.campaign.core.domain.models;

import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignName;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;

import java.math.BigDecimal;

public class CampaignValidator {

  public static void validateForCreation(
      CampaignName name,
      BigDecimal totalBudget) {
    
    if (name == null) {
      throw new BusinessRuleException("Campaign name is required");
    }
    
    if (totalBudget == null || totalBudget.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BusinessRuleException("Total budget must be greater than zero");
    }
  }

  public static void validateForUpdate(MarketingCampaign campaign) {
    if (campaign == null) {
      throw new BusinessRuleException("Campaign cannot be null");
    }
    
    if (campaign.getStatus() == CampaignStatus.COMPLETED) {
      throw new BusinessRuleException("Cannot update a completed campaign");
    }
    
    if (campaign.getStatus() == CampaignStatus.CANCELLED) {
      throw new BusinessRuleException("Cannot update a cancelled campaign");
    }
  }

  public static void validateForDeletion(MarketingCampaign campaign) {
    if (campaign == null) {
      throw new BusinessRuleException("Campaign cannot be null");
    }
    
    if (campaign.isActive()) {
      throw new BusinessRuleException(
          "Cannot delete an active campaign. Please pause or complete it first.");
    }
  }

  public static void validateSpendingAmount(BigDecimal amount, MarketingCampaign campaign) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BusinessRuleException("Spending amount must be greater than zero");
    }
    
    if (campaign.getBudget() == null) {
      throw new BusinessRuleException("Campaign has no budget configured");
    }
    
    BigDecimal newTotal = campaign.getBudget().spentAmount().add(amount);
    if (newTotal.compareTo(campaign.getBudget().totalBudget()) > 0) {
      throw new BusinessRuleException(
          String.format("Spending amount would exceed total budget. " +
              "Available: %s, Attempting to spend: %s",
              campaign.getBudget().remainingBudget(), amount));
    }
  }
}