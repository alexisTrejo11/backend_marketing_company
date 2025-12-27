package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.controller;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.mapper.AbTestOutputMapper;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CompleteAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CreateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.UpdateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.ab_test.core.port.input.AbTestCommandServicePort;
import at.backend.MarketingCompany.marketing.ab_test.core.port.input.AbTestQueryServicePort;
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
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AbTestMutationController {
	private final AbTestCommandServicePort abTestQueryServicePort;
	private final AbTestOutputMapper abTestMapper;

	@MutationMapping
	@GraphQLRateLimit("resource-mutation")
	public AbTestOutput createAbTest(@Argument @Valid @NotNull CreateAbTestInput input) {
		log.debug("GraphQL Mutation: createAbTest with name: {}", input.testName());

		CreateAbTestCommand command = input.toCommand();
		AbTest abTest = abTestQueryServicePort.createAbTest(command);

		return abTestMapper.toOutput(abTest);
	}

	@MutationMapping
	@GraphQLRateLimit("resource-mutation")
	public AbTestOutput updateAbTest(@Argument @Valid UpdateAbTestInput input) {
		log.debug("GraphQL Mutation: updateAbTest with id: {}", input.testId());

		UpdateAbTestCommand command = input.toCommand();
		AbTest abTest = abTestQueryServicePort.updateAbTest(command);

		return abTestMapper.toOutput(abTest);
	}

	@MutationMapping
	@GraphQLRateLimit("resource-mutation")
	public AbTestOutput completeAbTest(@Argument @Valid CompleteAbTestInput input) {
		log.debug("GraphQL Mutation: completeAbTest with id: {}", input.testId());

		CompleteAbTestCommand command = input.toCommand();
		AbTest abTest = abTestQueryServicePort.completeAbTest(command);

		return abTestMapper.toOutput(abTest);
	}

	@MutationMapping
	@GraphQLRateLimit("resource-mutation")
	public boolean deleteAbTest(@Argument @NotNull @Positive Long id) {
		log.debug("GraphQL Mutation: deleteAbTest with id: {}", id);

		var testId = new AbTestId(id);
		abTestQueryServicePort.deleteAbTest(testId);

		return true;
	}
}