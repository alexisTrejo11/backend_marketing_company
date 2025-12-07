package at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.controller;

import at.backend.MarketingCompany.common.PageResponse;
import at.backend.MarketingCompany.crm.interaction.application.InteractionApplicationService;
import at.backend.MarketingCompany.crm.interaction.application.commands.*;
import at.backend.MarketingCompany.crm.interaction.application.queries.*;
import at.backend.MarketingCompany.common.utils.PageInput;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.InteractionGraphQLMapper;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.input.AddFeedbackInput;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.input.CreateInteractionInput;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.input.InteractionFilterInput;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.input.UpdateInteractionInput;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.output.CustomerInteractionAnalyticsResponse;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.output.InteractionResponse;
import at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto.output.InteractionStatisticsResponse;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InteractionController {

  private final InteractionApplicationService interactionApplicationService;
  private final InteractionGraphQLMapper interactionGraphQLMapper;

  @QueryMapping
  public InteractionResponse interaction(@Argument String id) {
    log.debug("Fetching interaction by ID: {}", id);

    var query = GetInteractionByIdQuery.from(id);
    var interaction = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toGraphQLResponse(interaction);
  }

  @QueryMapping
  public List<InteractionResponse> interactions(@Argument PageInput pageInput,
      @Argument InteractionFilterInput filter) {
    log.debug("Fetching interactions with filter: {}", filter);

    var searchQuery = createSearchQuery(filter);
    var interactions = interactionApplicationService.handle(searchQuery);

    return interactionGraphQLMapper.toGraphQLResponseList(interactions);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> interactionsByCustomer(@Argument String customerId,
      @Argument PageInput pageInput) {
    log.debug("Fetching interactions for customer: {}", customerId);

    var query = GetInteractionsByCustomerQuery.from(customerId, pageInput.toPageable());
    var interactions = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toPaginatedResponse(interactions);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> interactionsByType(@Argument String type, @Argument PageInput pageInput) {
    log.debug("Fetching interactions by type: {}", type);

    var query = new GetInteractionsByTypeQuery(InteractionType.valueOf(type), pageInput.toPageable());
    var interactions = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toPaginatedResponse(interactions);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> interactionsByFeedbackType(@Argument String feedbackType,
      @Argument PageInput pageInput) {
    log.debug("Fetching interactions by feedback type: {}", feedbackType);

    var query = new GetInteractionsByFeedbackTypeQuery(
        FeedbackType.valueOf(feedbackType), pageInput.toPageable());
    var interactions = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toPaginatedResponse(interactions);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> recentInteractions(@Argument int days, @Argument PageInput pageInput) {
    log.debug("Fetching recent interactions from last {} days", days);

    var query = new GetRecentInteractionsQuery(days, pageInput.toPageable());
    var interactions = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toPaginatedResponse(interactions);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> todayInteractions(@Argument PageInput pageInput) {
    log.debug("Fetching today's interactions");

    var query = new GetTodayInteractionsQuery(pageInput.toPageable());
    var interactions = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toPaginatedResponse(interactions);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> interactionsRequiringFollowUp(@Argument PageInput pageInput) {
    log.debug("Fetching interactions requiring follow-up");

    var query = new GetInteractionsRequiringFollowUpQuery(pageInput.toPageable());
    var interactions = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toPaginatedResponse(interactions);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> positiveInteractions(@Argument PageInput pageInput) {
    log.debug("Fetching positive interactions");

    var query = new GetPositiveInteractionsQuery(pageInput.toPageable());
    var interactions = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toPaginatedResponse(interactions);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> negativeInteractions(@Argument PageInput pageInput) {
    log.debug("Fetching negative interactions");

    var query = new GetNegativeInteractionsQuery(pageInput.toPageable());
    var interactions = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toPaginatedResponse(interactions);
  }

  @QueryMapping
  public InteractionStatisticsResponse interactionStatistics(@Argument String customerId) {
    log.debug("Fetching interaction statistics for customer: {}", customerId);

    var query = GetInteractionStatisticsQuery.from(customerId);
    var statistics = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toStatisticsResponse(statistics);
  }

  @QueryMapping
  public CustomerInteractionAnalyticsResponse customerInteractionAnalytics(@Argument String customerId) {
    log.debug("Fetching customer interaction analytics for: {}", customerId);

    var query = new GetCustomerInteractionAnalyticsQuery(customerId);
    var analytics = interactionApplicationService.handle(query);

    return interactionGraphQLMapper.toAnalyticsResponse(analytics);
  }

  @MutationMapping
  public InteractionResponse createInteraction(@Valid @Argument CreateInteractionInput input) {
    log.info("Creating new interaction for customer: {}", input.customerId());

    var command = new CreateInteractionCommand(
        new CustomerId(input.customerId()),
        input.type(),
        at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionDateTime
            .from(input.dateTime()),
        at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionDescription
            .from(input.description()),
        at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionOutcome.from(input.outcome()),
        input.feedbackType(),
        at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.ChannelPreference
            .from(input.channelPreference()));

    var result = interactionApplicationService.handle(command);
    return interactionGraphQLMapper.toGraphQLResponse(result);
  }

  @MutationMapping
  public InteractionResponse updateInteraction(@Valid @Argument UpdateInteractionInput input) {
    log.info("Updating interaction: {}", input.interactionId());

    var command = new UpdateInteractionCommand(
        input.interactionId(),
        input.type(),
        at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionDateTime
            .from(input.dateTime()),
        at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionDescription
            .from(input.description()),
        at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionOutcome.from(input.outcome()));

    var result = interactionApplicationService.handle(command);
    return interactionGraphQLMapper.toGraphQLResponse(result);
  }

  @MutationMapping
  public InteractionResponse addFeedback(@Valid @Argument AddFeedbackInput input) {
    log.info("Adding feedback to interaction: {}", input.interactionId());

    var command = AddFeedbackCommand.from(
        input.interactionId(),
        input.feedbackType(),
        input.notes());

    var result = interactionApplicationService.handle(command);
    return interactionGraphQLMapper.toGraphQLResponse(result);
  }

  @MutationMapping
  public InteractionResponse markPositiveFeedback(@Argument String interactionId) {
    log.info("Marking interaction as positive feedback: {}", interactionId);

    var command = MarkPositiveFeedbackCommand.from(interactionId);
    var result = interactionApplicationService.handle(command);

    return interactionGraphQLMapper.toGraphQLResponse(result);
  }

  @MutationMapping
  public InteractionResponse markNegativeFeedback(@Argument String interactionId) {
    log.info("Marking interaction as negative feedback: {}", interactionId);

    var command = MarkNegativeFeedbackCommand.from(interactionId);
    var result = interactionApplicationService.handle(command);

    return interactionGraphQLMapper.toGraphQLResponse(result);
  }

  @MutationMapping
  public InteractionResponse markNeutralFeedback(@Argument String interactionId) {
    log.info("Marking interaction as neutral feedback: {}", interactionId);

    var command = MarkNeutralFeedbackCommand.from(interactionId);
    var result = interactionApplicationService.handle(command);

    return interactionGraphQLMapper.toGraphQLResponse(result);
  }

  @MutationMapping
  public Boolean deleteInteraction(@Argument String interactionId) {
    log.info("Deleting interaction: {}", interactionId);

    var command = DeleteInteractionCommand.from(interactionId);
    interactionApplicationService.handle(command);

    return true;
  }

  private SearchInteractionsQuery createSearchQuery(InteractionFilterInput filter) {
    if (filter == null) {
      return new SearchInteractionsQuery(null, null, null, null, null, null);
    }

    return new SearchInteractionsQuery(
        filter.searchTerm(),
        filter.customerId(),
        filter.type(),
        filter.feedbackType(),
        filter.startDate(),
        filter.endDate());
  }
}
