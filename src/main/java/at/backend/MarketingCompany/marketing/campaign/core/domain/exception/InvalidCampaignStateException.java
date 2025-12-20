package at.backend.MarketingCompany.marketing.campaign.core.domain.exception;

public class InvalidCampaignStateException extends MarketingDomainException {
  public InvalidCampaignStateException(String message) {
    super(message);
  }
}