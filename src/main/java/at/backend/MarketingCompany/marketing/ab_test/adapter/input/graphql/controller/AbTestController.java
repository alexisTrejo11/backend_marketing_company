package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.mapper.AbTestOutputMapper;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CompleteAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CreateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.UpdateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.ab_test.core.port.input.AbTestServicePort;
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
public class AbTestController {
    private final AbTestServicePort abTestServicePort;
    private final AbTestOutputMapper abTestMapper;

    @QueryMapping
    public AbTestOutput abTest(@Argument @NotNull @Positive Long id) {
        log.debug("GraphQL Query: abTest with id: {}", id);

        var testId = new AbTestId(id);
        AbTest abTest = abTestServicePort.getAbTestById(testId);

        return abTestMapper.toOutput(abTest);
    }

    @QueryMapping
    public PageResponse<AbTestOutput> searchAbTests(
        @Argument AbTestFilterInput filter,
        @Argument @Valid PageInput pageInput) {
        log.debug("GraphQL Query: searchAbTests with filter");

        AbTestQuery query = filter != null ? filter.toQuery() : AbTestQuery.empty();
        Pageable pageable = pageInput.toPageable();

        Page<AbTest> abTestPage = abTestServicePort.searchAbTests(query, pageable);

        return abTestMapper.toPageOutput(abTestPage);
    }

    @QueryMapping
    public PageResponse<AbTestOutput> abTestsByCampaign(
        @Argument @NotNull @Positive Long campaignId,
        @Argument @Valid PageInput pageInput) {
        log.debug("GraphQL Query: abTestsByCampaign with campaignId: {}", campaignId);

        var marketingCampaignId = new MarketingCampaignId(campaignId);
        Pageable pageable = pageInput.toPageable();

        Page<AbTest> abTestPage = abTestServicePort.getAbTestsByCampaign(marketingCampaignId, pageable);

        return abTestMapper.toPageOutput(abTestPage);
    }

    @QueryMapping
    public PageResponse<AbTestOutput> completedAbTests(@Argument @Valid PageInput pageInput) {
        log.debug("GraphQL Query: completedAbTests");

        Pageable pageable = pageInput.toPageable();
        Page<AbTest> abTestPage = abTestServicePort.getCompletedTests(pageable);

        return abTestMapper.toPageOutput(abTestPage);
    }

    @QueryMapping
    public PageResponse<AbTestOutput> runningAbTests(@Argument @Valid PageInput pageInput) {
        log.debug("GraphQL Query: runningAbTests");

        Pageable pageable = pageInput.toPageable();
        Page<AbTest> abTestPage = abTestServicePort.getRunningTests(pageable);

        return abTestMapper.toPageOutput(abTestPage);
    }

    @QueryMapping
    public PageResponse<AbTestOutput> scheduledAbTests(@Argument @Valid @NotNull PageInput pageInput) {
        log.debug("GraphQL Query: scheduledAbTests");

        Pageable pageable = pageInput.toPageable();
        Page<AbTest> abTestPage = abTestServicePort.getScheduledTests(pageable);

        return abTestMapper.toPageOutput(abTestPage);
    }

    @QueryMapping
    public AbTestStatistics abTestStatistics(@Argument @NotNull @Positive Long campaignId) {
        log.debug("GraphQL Query: abTestStatistics for campaignId: {}", campaignId);

        var marketingCampaignId = new MarketingCampaignId(campaignId);
        return abTestServicePort.getAbTestStatistics(marketingCampaignId);
    }

    @QueryMapping
    public Double abTestCompletionRate(@Argument @NotNull @Positive Long campaignId) {
        log.debug("GraphQL Query: abTestCompletionRate for campaignId: {}", campaignId);

        var marketingCampaignId = new MarketingCampaignId(campaignId);
        return abTestServicePort.getCompletionRate(marketingCampaignId);
    }

    @QueryMapping
    public Float abTestAverageSignificance(@Argument @NotNull @Positive Long campaignId) {
        log.debug("GraphQL Query: abTestAverageSignificance for campaignId: {}", campaignId);

        var marketingCampaignId = new MarketingCampaignId(campaignId);
        return abTestServicePort.getAverageSignificance(marketingCampaignId).floatValue();
    }

    @MutationMapping
    public AbTestOutput createAbTest(@Argument @Valid CreateAbTestInput input) {
        log.debug("GraphQL Mutation: createAbTest with name: {}", input.testName());
        
        CreateAbTestCommand command = input.toCommand();
        AbTest abTest = abTestServicePort.createAbTest(command);
        
        return abTestMapper.toOutput(abTest);
    }

    @MutationMapping
    public AbTestOutput updateAbTest(@Argument @Valid UpdateAbTestInput input) {
        log.debug("GraphQL Mutation: updateAbTest with id: {}", input.testId());
        
        UpdateAbTestCommand command = input.toCommand();
        AbTest abTest = abTestServicePort.updateAbTest(command);
        
        return abTestMapper.toOutput(abTest);
    }

    @MutationMapping
    public AbTestOutput completeAbTest(@Argument @Valid CompleteAbTestInput input) {
        log.debug("GraphQL Mutation: completeAbTest with id: {}", input.testId());
        
        CompleteAbTestCommand command = input.toCommand();
        AbTest abTest = abTestServicePort.completeAbTest(command);
        
        return abTestMapper.toOutput(abTest);
    }

    @MutationMapping
    public boolean deleteAbTest(@Argument @NotNull @Positive Long id) {
        log.debug("GraphQL Mutation: deleteAbTest with id: {}", id);
        
        var testId = new AbTestId(id);
        abTestServicePort.deleteAbTest(testId);
        
        return true;
    }
}