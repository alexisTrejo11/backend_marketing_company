package at.backend.MarketingCompany.marketing.activity.core.domain.exception;

public class ActivityScheduleException extends CampaignActivityException {
	public ActivityScheduleException(String message) {
		super(message, "CAMPAIGN_ACTIVITY_SCHEDULE_ERROR");
	}
}
