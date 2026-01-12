package at.backend.MarketingCompany.marketing.campaign.core.domain.exception;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class MarketingCampaignNotFoundException extends NotFoundException {

	public MarketingCampaignNotFoundException(MarketingCampaignId id) {
		super(id.asString(), "Marketing Campaign");
	}
}
