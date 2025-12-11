package at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto;

import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.crm.interaction.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.ChannelPreference;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionDescription;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.output.CustomerInteractionAnalyticsResponse;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.output.InteractionResponse;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.output.InteractionStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InteractionGraphQLMapper {
    
    private final ExternalDataFetcher externalDataFetcher;

    public InteractionResponse toGraphQLResponse(Interaction interaction) {
        if (interaction == null) return null;

        var customer = externalDataFetcher.fetchCustomerInfo(interaction.getCustomerCompanyId());

        return new InteractionResponse(
            interaction.getId().value(),
            interaction.getCustomerCompanyId().value(),
            customer,
            interaction.getType(),
            interaction.getDateTime().value(),
            interaction.getDescription().map(InteractionDescription::value).orElse(null),
            interaction.getOutcome().value(),
            interaction.getFeedbackType().orElse(null),
            interaction.getChannelPreference().map(ChannelPreference::value).orElse(null),
            interaction.hasFeedback(),
            interaction.isPositiveFeedback(),
            interaction.isNegativeFeedback(),
            interaction.isRecent(),
            interaction.getFeedbackType().map(FeedbackType::requiresFollowUp).orElse(false),
            interaction.getCreatedAt(),
            interaction.getUpdatedAt()
        );
    }

    public List<InteractionResponse> toGraphQLResponseList(List<Interaction> interactions) {
        return interactions.stream()
            .map(this::toGraphQLResponse)
            .toList();
    }

    public PageResponse<InteractionResponse> toPaginatedResponse(Page<Interaction> interactionPage) {
        Page<InteractionResponse> responsePage = interactionPage.map(this::toGraphQLResponse);
        return PageResponse.of(responsePage);
    }


    public InteractionStatisticsResponse toStatisticsResponse(
            at.backend.MarketingCompany.crm.interaction.application.InteractionApplicationService.InteractionStatistics statistics) {
        return new InteractionStatisticsResponse(
            statistics.totalInteractions(),
            statistics.recentInteractions(),
            statistics.positiveInteractions(),
            statistics.negativeInteractions(),
            statistics.followUpRequired()
        );
    }

    public CustomerInteractionAnalyticsResponse toAnalyticsResponse(
            at.backend.MarketingCompany.crm.interaction.application.InteractionApplicationService.CustomerInteractionAnalytics analytics) {
        return new CustomerInteractionAnalyticsResponse(
            analytics.customerId(),
            analytics.frequentInteractionTypes(),
            analytics.predominantFeedback(),
            analytics.totalInteractions(),
            analytics.monthlyFrequency()
        );
    }
}