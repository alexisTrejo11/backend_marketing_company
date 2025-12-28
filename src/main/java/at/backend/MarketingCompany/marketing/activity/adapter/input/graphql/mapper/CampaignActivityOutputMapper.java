package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.output.CampaignActivityOutput;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CampaignActivityOutputMapper {

	public CampaignActivityOutput toOutput(CampaignActivity activity) {
		if (activity == null) {
			return null;
		}

		var builder = CampaignActivityOutput.builder()
				.id(activity.getId().getValue())
				.campaignId(activity.getCampaignId().getValue())
				.name(activity.getName())
				.description(activity.getDescription())
				.activityType(activity.getActivityType())
				.status(activity.getStatus())
				.targetAudience(activity.getTargetAudience())
				.dependenciesJson(activity.getDependencies() != null ? activity.getDependencies().toString() : null)
				.createdAt(activity.getCreatedAt() != null ? activity.getCreatedAt().toString() : null)
				.updatedAt(activity.getUpdatedAt() != null ? activity.getUpdatedAt().toString() : null);

		if (activity.getSchedule() != null) {
			builder.plannedStartDate(activity.getSchedule().plannedStartDate())
					.plannedEndDate(activity.getSchedule().plannedEndDate())
					.actualStartDate(activity.getSchedule().actualStartDate())
					.actualEndDate(activity.getSchedule().actualEndDate());
		}

		if (activity.getCost() != null) {
			builder.plannedCost(activity.getCost().plannedCost())
					.actualCost(activity.getCost().actualCost());
		}


		return builder.build();
	}

	public PageResponse<CampaignActivityOutput> toPageResponse(Page<CampaignActivity> pageResponse) {
		if (pageResponse == null) {
			return PageResponse.empty();
		}

		return PageResponse.of(
				pageResponse.map(this::toOutput)
		);
	}
}
