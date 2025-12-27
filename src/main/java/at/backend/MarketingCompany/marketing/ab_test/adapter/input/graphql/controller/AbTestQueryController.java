package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.controller;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.mapper.AbTestOutputMapper;
import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.ab_test.core.port.input.AbTestQueryServicePort;
import at.backend.MarketingCompany.marketing.ab_test.core.port.input.AbTestStatisticsServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AbTestQueryController {
	private final AbTestQueryServicePort abTestQueryServicePort;
	private final AbTestStatisticsServicePort abTestStatisticsServicePort;
	private final AbTestOutputMapper abTestMapper;

	@QueryMapping
	@GraphQLRateLimit
	public AbTestOutput abTest(@Argument @NotNull @Positive Long id) {
		log.debug("GraphQL Query: abTest with id: {}", id);

		var testId = new AbTestId(id);
		AbTest abTest = abTestQueryServicePort.getAbTestById(testId);

		return abTestMapper.toOutput(abTest);
	}

	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<AbTestOutput> searchAbTests(
			@Argument AbTestFilterInput filter,
			@Argument @Valid PageInput pageInput) {
		log.debug("GraphQL Query: searchAbTests with filter");

		AbTestQuery query = filter != null ? filter.toQuery() : AbTestQuery.empty();
		Pageable pageable = pageInput.toPageable();

		Page<AbTest> abTestPage = abTestQueryServicePort.searchAbTests(query, pageable);

		return abTestMapper.toPageOutput(abTestPage);
	}

	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<AbTestOutput> abTestsByCampaign(
			@Argument @NotNull @Positive Long campaignId,
			@Argument @Valid PageInput pageInput) {
		log.debug("GraphQL Query: abTestsByCampaign with campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		Pageable pageable = pageInput.toPageable();

		Page<AbTest> abTestPage = abTestQueryServicePort.getAbTestsByCampaign(marketingCampaignId, pageable);

		return abTestMapper.toPageOutput(abTestPage);
	}

	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<AbTestOutput> completedAbTests(@Argument @Valid PageInput pageInput) {
		log.debug("GraphQL Query: completedAbTests");

		Pageable pageable = pageInput.toPageable();
		Page<AbTest> abTestPage = abTestQueryServicePort.getCompletedTests(pageable);

		return abTestMapper.toPageOutput(abTestPage);
	}

	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<AbTestOutput> runningAbTests(@Argument @Valid PageInput pageInput) {
		log.debug("GraphQL Query: runningAbTests");

		Pageable pageable = pageInput.toPageable();
		Page<AbTest> abTestPage = abTestQueryServicePort.getRunningTests(pageable);

		return abTestMapper.toPageOutput(abTestPage);
	}

	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<AbTestOutput> scheduledAbTests(@Argument @Valid @NotNull PageInput pageInput) {
		log.debug("GraphQL Query: scheduledAbTests");

		Pageable pageable = pageInput.toPageable();
		Page<AbTest> abTestPage = abTestQueryServicePort.getScheduledTests(pageable);

		return abTestMapper.toPageOutput(abTestPage);
	}

	@QueryMapping
	@GraphQLRateLimit
	public AbTestStatisticsOutput abTestStatistics(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: abTestStatisticsServicePort for campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		AbTestStatistics statistics = abTestStatisticsServicePort.getAbTestStatistics(marketingCampaignId);
		return abTestMapper.toStatisticsOutput(statistics);
	}

	@QueryMapping
	@GraphQLRateLimit
	public Double abTestCompletionRate(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: abTestCompletionRate for campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		return abTestStatisticsServicePort.getCompletionRate(marketingCampaignId);
	}

	@QueryMapping
	@GraphQLRateLimit
	public Float abTestAverageSignificance(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: abTestAverageSignificance for campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		return abTestStatisticsServicePort.getAverageSignificance(marketingCampaignId).floatValue();
	}

}