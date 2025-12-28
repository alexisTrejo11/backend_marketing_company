package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.activity.core.application.command.RecordActivityDatesCommand;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;

import java.time.LocalDateTime;

public record RecordActivityDatesInput(
    Long activityId,
    LocalDateTime actualStartDate,
    LocalDateTime actualEndDate
) {
		public RecordActivityDatesCommand toCommand() {
				return new RecordActivityDatesCommand(
						new CampaignActivityId(activityId),
						actualStartDate,
						actualEndDate
				);
		}
}