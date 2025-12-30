package at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto.CampaignMetricOutput;
import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto.CreateMetricInput;
import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto.UpdateMetricInput;
import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto.UpdateMetricValueInput;
import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.mapper.MetricOutputMapper;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import at.backend.MarketingCompany.marketing.metric.core.port.input.MetricCommandInputPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CampaignMetricMutationController {
	private final MetricCommandInputPort commandPort;
	private final MetricOutputMapper mapper;

	@MutationMapping
	public CampaignMetricOutput createMetric(@Argument @Valid @NotNull CreateMetricInput input) {
		var command = input.toCommand();
		CampaignMetric metric = commandPort.createMetric(command);

		return mapper.toOutput(metric);
	}

	@MutationMapping
	public CampaignMetricOutput updateGeneralInfoMetric(@Argument @Valid @NotNull UpdateMetricInput input) {
		var command = input.toCommand();
		CampaignMetric metric = commandPort.updateMetric(command);

		return mapper.toOutput(metric);
	}

	@MutationMapping
	public CampaignMetricOutput updateMetricValue(@Argument @Valid @NotNull UpdateMetricValueInput input) {
		var command = input.toCommand();
		CampaignMetric metric = commandPort.updateMetricValue(command);

		return mapper.toOutput(metric);
	}

	@MutationMapping
	public CampaignMetricOutput markAsAutomated(@Argument @Positive @NotNull @Positive Long metricId) {
		var campaignMetricId = new CampaignMetricId(metricId);
		CampaignMetric metric = commandPort.markAsAutomated(campaignMetricId);
		return mapper.toOutput(metric);
	}

	@MutationMapping
	public boolean deleteMetric(@Argument @Positive @NotNull Long metricId) {
		var campaignMetricId = new CampaignMetricId(metricId);
		commandPort.deleteMetric(campaignMetricId);
		return true;
	}
}


