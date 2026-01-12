package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.controller;

import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.input.*;
import at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.output.*;
import at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.mapper.InteractionResponseMapper;
import at.backend.MarketingCompany.crm.interaction.core.application.commands.*;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.*;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;
import at.backend.MarketingCompany.crm.interaction.core.ports.input.InteractionCommandService;
import at.backend.MarketingCompany.crm.interaction.core.ports.input.InteractionQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InteractionController {
  private final InteractionCommandService commandService;
  private final InteractionQueryService queryService;
  private final InteractionResponseMapper responseMapper;

  @QueryMapping
  public InteractionResponse interaction(@Argument @Valid @NotBlank String interactionId) {
    var query = GetInteractionByIdQuery.from(interactionId);
    Interaction interaction = queryService.getInteractionById(query);

    return responseMapper.toResponse(interaction);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> interactionsByCustomer(
      @Argument @Valid GetInteractionsByCustomerInput input) {

    GetInteractionsByCustomerQuery query = input.toQuery();
    Page<Interaction> interactionPage = queryService.getInteractionsByCustomer(query);

    return responseMapper.toPaginatedResponse(interactionPage);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> interactionsByType(
      @Argument InteractionType type,
      @Argument PageInput pageInput) {

    var query = new GetInteractionsByTypeQuery(type, pageInput.toPageable());
    Page<Interaction> interactionPage = queryService.getInteractionsByType(query);

    return responseMapper.toPaginatedResponse(interactionPage);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> interactionsByFeedbackType(
      @Argument FeedbackType feedbackType,
      @Argument PageInput pageInput) {

    var query = GetInteractionsByFeedbackTypeQuery.of(feedbackType, pageInput);
    Page<Interaction> ineractionPage = queryService.getInteractionsByFeedbackType(query);

    return responseMapper.toPaginatedResponse(ineractionPage);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> recentInteractions(@Argument int days, @Argument PageInput pageInput) {
    var query = new GetRecentInteractionsQuery(days, pageInput.toPageable());
    var interactionPage = queryService.getRecentInteractions(query);

    return responseMapper.toPaginatedResponse(interactionPage);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> todayInteractions(@Argument PageInput pageInput) {
    var query = new GetTodayInteractionsQuery(pageInput.toPageable());
    var interactionPage = queryService.getTodayInteractions(query);

    return responseMapper.toPaginatedResponse(interactionPage);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> interactionsRequiringFollowUp(@Argument PageInput pageInput) {
    var query = new GetInteractionsRequiringFollowUpQuery(pageInput.toPageable());
    var interactionPage = queryService.getInteractionsRequiringFollowUp(query);

    return responseMapper.toPaginatedResponse(interactionPage);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> positiveInteractions(@Argument PageInput pageInput) {
    var query = new GetPositiveInteractionsQuery(pageInput.toPageable());
    var interactionPage = queryService.getPositiveInteractions(query);

    return responseMapper.toPaginatedResponse(interactionPage);
  }

  @QueryMapping
  public PageResponse<InteractionResponse> negativeInteractions(@Argument PageInput pageInput) {
    var query = new GetNegativeInteractionsQuery(pageInput.toPageable());
    var interactionPage = queryService.getNegativeInteractions(query);

    return responseMapper.toPaginatedResponse(interactionPage);
  }

  @QueryMapping
  public InteractionStatisticsResponse interactionStatistics(@Argument String customerId) {
    var query = GetInteractionStatisticsQuery.from(customerId);
    var statistics = queryService.getInteractionStatistics(query);

    return responseMapper.toStatisticsResponse(statistics);
  }

  @QueryMapping
  public CustomerInteractionAnalyticsResponse customerInteractionAnalytics(@Argument String customerId) {
    var query = GetCustomerInteractionAnalyticsQuery.from(customerId);
    var analytics = queryService.getCustomerInteractionAnalytics(query);

    return responseMapper.toAnalyticsResponse(analytics);
  }

  @MutationMapping
  public InteractionResponse createInteraction(@Valid @Argument CreateInteractionInput input) {
    CreateInteractionCommand command = input.toCommand();
    var result = commandService.createInteraction(command);

    return responseMapper.toResponse(result);
  }

  @MutationMapping
  public InteractionResponse updateInteraction(@Valid @Argument UpdateInteractionInput input) {
    UpdateInteractionCommand command = input.toCommand();
    var result = commandService.updateInteraction(command);
    return responseMapper.toResponse(result);
  }

  @MutationMapping
  public InteractionResponse addFeedback(@Valid @Argument AddFeedbackInput input) {
    AddFeedbackCommand command = input.toCommand();
    Interaction interactionUpdated = commandService.addFeedback(command);

    return responseMapper.toResponse(interactionUpdated);
  }

  @MutationMapping
  public InteractionResponse markPositiveFeedback(@Argument String interactionId) {
    var command = MarkPositiveFeedbackCommand.from(interactionId);
    var result = commandService.markAsPositiveFeedback(command);

    return responseMapper.toResponse(result);
  }

  @MutationMapping
  public InteractionResponse markNegativeFeedback(@Argument String interactionId) {
    var command = MarkNegativeFeedbackCommand.from(interactionId);
    var result = commandService.markAsNegativeFeedback(command);

    return responseMapper.toResponse(result);
  }

  @MutationMapping
  public InteractionResponse markNeutralFeedback(@Argument String interactionId) {
    var command = MarkNeutralFeedbackCommand.from(interactionId);
    var result = commandService.markAsNeutralFeedback(command);

    return responseMapper.toResponse(result);
  }

  @MutationMapping
  public Boolean deleteInteraction(@Argument String interactionId) {
    var command = DeleteInteractionCommand.from(interactionId);
    commandService.deleteInteraction(command);

    return true;
  }
}
