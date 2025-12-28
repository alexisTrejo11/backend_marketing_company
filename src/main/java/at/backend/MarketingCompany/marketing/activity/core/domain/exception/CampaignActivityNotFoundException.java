package at.backend.MarketingCompany.marketing.activity.core.domain.exception;

import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class CampaignActivityNotFoundException extends NotFoundException {
	public CampaignActivityNotFoundException(CampaignActivityId id) {
		super("ID", id.getValue().toString());
	}
}
