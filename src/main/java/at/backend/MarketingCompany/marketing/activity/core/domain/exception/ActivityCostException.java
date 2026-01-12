package at.backend.MarketingCompany.marketing.activity.core.domain.exception;

public class ActivityCostException extends ActivityValidationException {
	public ActivityCostException(String message) {
		super(message, "CAMPAIGN_ACTIVITY_COST_ERROR");
	}
}
