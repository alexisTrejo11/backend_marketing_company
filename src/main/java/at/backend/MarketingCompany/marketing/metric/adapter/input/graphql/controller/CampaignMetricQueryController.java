package at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto.CampaignMetricOutput;
import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto.MetricQueryInput;
import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.mapper.MetricOutputMapper;
import at.backend.MarketingCompany.marketing.metric.core.application.dto.MetricStatistics;
import at.backend.MarketingCompany.marketing.metric.core.application.query.MetricQuery;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import at.backend.MarketingCompany.marketing.metric.core.port.input.MetricQueryInputPort;
import at.backend.MarketingCompany.marketing.metric.core.port.input.MetricStatisticInputPort;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class CampaignMetricQueryController {
	private final MetricQueryInputPort queryPort;
	private final MetricStatisticInputPort statisticPort;
	private final MetricOutputMapper mapper;


	@QueryMapping
	public CampaignMetricOutput campaignMetric(@Argument @Valid @NotNull @Positive Long metricId) {
		CampaignMetricId metricIdObj = new CampaignMetricId(metricId);

		CampaignMetric metricPage = queryPort.getMetricById(metricIdObj);

		return mapper.toOutput(metricPage);
	}

	@QueryMapping
	public PageResponse<CampaignMetricOutput> campaignMetrics(@Argument @Valid @NotNull PageInput pageable,
	                                                          @Argument @Valid @NotNull MetricQueryInput input) {
		Pageable pageRequest = pageable.toPageable();
		MetricQuery query = input.toQuery();

		Page<CampaignMetric> metricPage = queryPort.searchMetrics(query, pageRequest);

		return mapper.toPageResponse(metricPage);

	}

	@QueryMapping
	public PageResponse<CampaignMetricOutput> campaignMetricsByCampaign(@Argument @Valid @NotNull PageInput pageable,
	                                                                    @Argument @Valid @NotNull Long campaignId) {
		Pageable pageRequest = pageable.toPageable();
		var campaignIdObj = new MarketingCampaignId(campaignId);

		Page<CampaignMetric> metricPage = queryPort.getMetricsByCampaign(campaignIdObj, pageRequest);

		return mapper.toPageResponse(metricPage);
	}

	@QueryMapping
	public PageResponse<CampaignMetricOutput> achievedCampaignMetrics(@Argument @Valid @NotNull PageInput pageable,
	                                                                  @Argument @Valid @NotNull Long campaignId) {
		Pageable pageRequest = pageable.toPageable();
		var campaignIdObj = new MarketingCampaignId(campaignId);
		Page<CampaignMetric> metricPage = queryPort.getAchievedMetrics(campaignIdObj, pageRequest);

		return mapper.toPageResponse(metricPage);
	}

	@QueryMapping
	public PageResponse<CampaignMetricOutput> notAchievedCampaignMetrics(@Argument @Valid @NotNull PageInput pageable,
	                                                                     @Argument @Valid @NotNull Long campaignId) {
		Pageable pageRequest = pageable.toPageable();
		var campaignIdObj = new MarketingCampaignId(campaignId);

		Page<CampaignMetric> metricPage = queryPort.getNotAchievedMetrics(campaignIdObj, pageRequest);

		return mapper.toPageResponse(metricPage);
	}

	@QueryMapping
	public PageResponse<CampaignMetricOutput> automatedCampaignMetrics(@Argument @Valid @NotNull PageInput pageable,
	                                                                   @Argument @Valid @NotNull Long campaignId) {
		Pageable pageRequest = pageable.toPageable();
		var campaignIdObj = new MarketingCampaignId(campaignId);

		Page<CampaignMetric> metricPage = queryPort.getAutomatedMetrics(campaignIdObj, pageRequest);

		return mapper.toPageResponse(metricPage);
	}

	@QueryMapping
	public PageResponse<CampaignMetricOutput> recentlyCampaignUpdatedMetrics(@Argument @Valid @NotNull PageInput pageable,
	                                                                         @Argument @Valid LocalDateTime fromDate) {
		Pageable pageRequest = pageable.toPageable();

		Page<CampaignMetric> metricPage = queryPort.getRecentlyUpdatedMetrics(fromDate, pageRequest);

		return mapper.toPageResponse(metricPage);
	}

	@QueryMapping
	public MetricStatistics campaignMetricStatistics(@Argument @Valid @NotNull Long campaignId) {
		var campaignIdObj = new MarketingCampaignId(campaignId);

		return statisticPort.getMetricStatistics(campaignIdObj);
	}
}
