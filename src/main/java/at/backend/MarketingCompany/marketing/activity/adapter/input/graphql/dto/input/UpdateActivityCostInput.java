package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCostCommand;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;

import java.math.BigDecimal;

public record UpdateActivityCostInput(
    Long activityId,
    BigDecimal actualCost,
    String costNotes
) {
	public UpdateActivityCostCommand toCommand() {
		return new UpdateActivityCostCommand(
			new CampaignActivityId(activityId),
			actualCost,
			costNotes
		);
	}
}