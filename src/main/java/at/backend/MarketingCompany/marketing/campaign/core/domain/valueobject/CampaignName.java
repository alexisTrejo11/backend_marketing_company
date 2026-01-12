package at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject;

public record CampaignName(String value) {
	public CampaignName {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("Campaign name cannot be empty");
		}
		if (value.length() > 100) {
			throw new IllegalArgumentException("Campaign name cannot exceed 100 characters");
		}
	}
}