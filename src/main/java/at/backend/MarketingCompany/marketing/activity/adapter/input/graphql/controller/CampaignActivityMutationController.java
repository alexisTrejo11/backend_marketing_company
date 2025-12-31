package at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input.CreateCampaignActivityInput;
import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input.RecordActivityDatesInput;
import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input.UpdateActivityCostInput;
import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.input.UpdateCampaignActivityInput;
import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.dto.output.CampaignActivityOutput;
import at.backend.MarketingCompany.marketing.activity.adapter.input.graphql.mapper.CampaignActivityOutputMapper;
import at.backend.MarketingCompany.marketing.activity.core.application.command.CreateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.RecordActivityDatesCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCostCommand;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.activity.core.port.input.CampaignActivityCommandServicePort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CampaignActivityMutationController {
	private final CampaignActivityCommandServicePort commandServicePort;
	private final CampaignActivityOutputMapper outputMapper;

	@MutationMapping
	public CampaignActivityOutput createCampaignActivity(@Argument @NotNull @Valid CreateCampaignActivityInput input) {
		CreateActivityCommand command = input.toCommand();
		CampaignActivity activity = commandServicePort.createActivity(command);

		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public CampaignActivityOutput updateCampaignActivity(@Argument @NotNull @Valid UpdateCampaignActivityInput input) {
		UpdateActivityCommand command = input.toCommand();
		CampaignActivity activity = commandServicePort.updateActivity(command);
		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public CampaignActivityOutput updateCampaignActivityCost(@Argument @Valid @NotNull UpdateActivityCostInput input) {
		UpdateActivityCostCommand command = input.toCommand();
		CampaignActivity activity = commandServicePort.updateActivityCost(command);

		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public CampaignActivityOutput updateCampaignActivitySchedule(@Argument @Valid @NotNull RecordActivityDatesInput input) {
		RecordActivityDatesCommand command = input.toCommand();
		CampaignActivity activity = commandServicePort.recordActivityDates(command);

		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public CampaignActivityOutput startCampaignActivity(@Argument @Valid @NotNull Long id) {
		CampaignActivityId campaignActivityId = new CampaignActivityId(id);
		CampaignActivity activity = commandServicePort.startActivity(campaignActivityId);

		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public CampaignActivityOutput cancelCampaignActivity(@Argument @Valid @NotNull Long id, @Argument String cancelReason) {
		CampaignActivityId campaignActivityId = new CampaignActivityId(id);
		CampaignActivity activity = commandServicePort.cancelActivity(campaignActivityId, cancelReason);

		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public CampaignActivityOutput completeCampaignActivity(@Argument @Valid @NotNull Long id, @Argument String completionNotes) {
		CampaignActivityId campaignActivityId = new CampaignActivityId(id);
		CampaignActivity activity = commandServicePort.completeActivity(campaignActivityId, completionNotes);

		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public CampaignActivityOutput blockCampaignActivity(@Argument @Valid @NotNull Long id) {
		CampaignActivityId campaignActivityId = new CampaignActivityId(id);
		CampaignActivity activity = commandServicePort.blockActivity(campaignActivityId, "Blocked via GraphQL Mutation");

		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public CampaignActivityOutput assignCampaignActivityToUser(@Argument @Valid @NotNull Long id, @Argument @Valid @NotNull Long userId) {
		CampaignActivityId campaignActivityId = new CampaignActivityId(id);
		CampaignActivity activity = commandServicePort.assignToUser(campaignActivityId, userId);

		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public CampaignActivityOutput unassignCampaignActivity(@Argument @Valid @NotNull Long id) {
		CampaignActivityId campaignActivityId = new CampaignActivityId(id);
		CampaignActivity activity = commandServicePort.unassign(campaignActivityId);

		return outputMapper.toOutput(activity);
	}

	@MutationMapping
	public boolean deleteCampaignActivity(@Argument @Valid @NotNull Long id) {
		CampaignActivityId campaignActivityId = new CampaignActivityId(id);
		commandServicePort.deleteActivity(campaignActivityId);

		return true;
	}


}
