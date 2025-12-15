package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.output.CustomerInteractionAnalyticsResponse;
import at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.output.InteractionResponse;
import at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.output.InteractionStatisticsResponse;
import at.backend.MarketingCompany.crm.interaction.core.application.analytics.CustomerInteractionAnalytics;
import at.backend.MarketingCompany.crm.interaction.core.application.analytics.InteractionStatistics;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.ChannelPreference;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionDescription;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InteractionResponseMapper {
  public InteractionResponse toResponse(Interaction interaction) {
    if (interaction == null)
      return null;

    var description = interaction.getDescription().map(InteractionDescription::value).orElse(null);
    var dateTime = interaction.getDateTime() != null ? interaction.getDateTime().value() : null;
    var requiresFollowUp = interaction.getFeedbackType().map(FeedbackType::requiresFollowUp).orElse(false);
    var channelPreference = interaction.getChannelPreference().map(ChannelPreference::value).orElse(null);
    var customerCompanyId = interaction.getCustomerCompanyId() != null
        ? interaction.getCustomerCompanyId().value()
        : null;

    var createdAt = interaction.getCreatedAt() != null
        ? interaction.getCreatedAt().atOffset(OffsetDateTime.now().getOffset())
        : null;

    var updatedAt = interaction.getUpdatedAt() != null
        ? interaction.getUpdatedAt().atOffset(OffsetDateTime.now().getOffset())
        : null;

    return InteractionResponse.builder()
        .id(interaction.getId() != null ? interaction.getId().value() : null)
        .customerId(customerCompanyId)
        .type(interaction.getType())
        .dateTime(dateTime)
        .description(description)
        .outcome(interaction.getOutcome() != null ? interaction.getOutcome().value() : null)
        .feedbackType(interaction.getFeedbackType().orElse(null))
        .channelPreference(channelPreference)
        .hasFeedback(interaction.hasFeedback())
        .isPositiveFeedback(interaction.isPositiveFeedback())
        .isNegativeFeedback(interaction.isNegativeFeedback())
        .isRecent(interaction.isRecent())
        .requiresFollowUp(requiresFollowUp)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .build();
  }

  public List<InteractionResponse> toResponseList(List<Interaction> interactions) {
    return interactions.stream()
        .map(this::toResponse)
        .toList();
  }

  public PageResponse<InteractionResponse> toPaginatedResponse(Page<Interaction> interactionPage) {
    Page<InteractionResponse> responsePage = interactionPage.map(this::toResponse);
    return PageResponse.of(responsePage);
  }

  public InteractionStatisticsResponse toStatisticsResponse(InteractionStatistics statistics) {
    return new InteractionStatisticsResponse(
        statistics.totalInteractions(),
        statistics.recentInteractions(),
        statistics.positiveInteractions(),
        statistics.negativeInteractions(),
        statistics.followUpRequired());
  }

  public CustomerInteractionAnalyticsResponse toAnalyticsResponse(CustomerInteractionAnalytics analytics) {
    return new CustomerInteractionAnalyticsResponse(
        analytics.customerId(),
        analytics.frequentInteractionTypes(),
        analytics.predominantFeedback(),
        analytics.totalInteractions(),
        analytics.monthlyFrequency());
  }
}
