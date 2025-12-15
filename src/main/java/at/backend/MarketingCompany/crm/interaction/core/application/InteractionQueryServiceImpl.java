package at.backend.MarketingCompany.crm.interaction.core.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.crm.interaction.core.application.analytics.CustomerInteractionAnalytics;
import at.backend.MarketingCompany.crm.interaction.core.application.analytics.InteractionStatistics;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.*;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionType;
import at.backend.MarketingCompany.crm.interaction.core.domain.exceptions.InteractionNotFoundException;
import at.backend.MarketingCompany.crm.interaction.core.ports.input.InteractionQueryService;
import at.backend.MarketingCompany.crm.interaction.core.ports.output.InteractionRepository;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class InteractionQueryServiceImpl implements InteractionQueryService {
  private final InteractionRepository interactionRepository;

  @Override
  @Transactional(readOnly = true)
  public Interaction getInteractionById(GetInteractionByIdQuery query) {
    log.debug("Fetching interaction by ID: {}", query.interactionId());

    return interactionRepository.findById(query.interactionId())
        .orElseThrow(() -> new InteractionNotFoundException(query.interactionId()));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getInteractionsByCustomer(GetInteractionsByCustomerQuery query) {
    log.debug("Fetching interactions for customer: {}", query.customerCompanyId());

    return interactionRepository.findByCustomer(query.customerCompanyId(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getInteractionsByType(GetInteractionsByTypeQuery query) {
    log.debug("Fetching interactions by type: {}", query.type());

    return interactionRepository.findByType(query.type(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getInteractionsByFeedbackType(GetInteractionsByFeedbackTypeQuery query) {
    log.debug("Fetching interactions by feedback type: {}", query.feedbackType());

    return interactionRepository.findByFeedbackType(query.feedbackType(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getInteractionsByCustomerAndType(GetInteractionsByCustomerAndTypeQuery query) {
    log.debug("Fetching interactions for customer {} and type: {}", query.customerCompanyId(), query.type());

    return interactionRepository.findByCustomerAndType(query.customerCompanyId(), query.type(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getInteractionsByDateRange(GetInteractionsByDateRangeQuery query) {
    log.debug("Fetching interactions between {} and {}", query.startDate(), query.endDate());

    return interactionRepository.findByDateRange(query.startDate(), query.endDate(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getRecentInteractions(GetRecentInteractionsQuery query) {
    log.debug("Fetching recent interactions from last {} days", query.days());

    return interactionRepository.findRecentInteractions(query.days(), query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getTodayInteractions(GetTodayInteractionsQuery query) {
    log.debug("Fetching today's interactions");

    return interactionRepository.findTodayInteractions(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getInteractionsRequiringFollowUp(GetInteractionsRequiringFollowUpQuery query) {
    log.debug("Fetching interactions requiring follow-up");

    return interactionRepository.findInteractionsRequiringFollowUp(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getPositiveInteractions(GetPositiveInteractionsQuery query) {
    log.debug("Fetching positive interactions");

    return interactionRepository.findPositiveInteractions(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> getNegativeInteractions(GetNegativeInteractionsQuery query) {
    log.debug("Fetching negative interactions");

    return interactionRepository.findNegativeInteractions(query.pageable());
  }

  @Override
  @Transactional(readOnly = true)
  public InteractionStatistics getInteractionStatistics(GetInteractionStatisticsQuery query) {
    log.debug("Fetching interaction statistics for customer: {}", query.customerCompanyId());

    long totalInteractions = interactionRepository.findByCustomer(query.customerCompanyId()).size();
    long recentInteractions = interactionRepository.countRecentInteractionsByCustomer(query.customerCompanyId(), 30);
    long positiveInteractions = interactionRepository.countByCustomerAndFeedbackType(query.customerCompanyId(),
        FeedbackType.POSITIVE);
    long negativeInteractions = interactionRepository.countByCustomerAndFeedbackType(query.customerCompanyId(),
        FeedbackType.NEGATIVE);
    long followUpRequired = interactionRepository.findInteractionsRequiringFollowUp().stream()
        .filter(interaction -> interaction.getCustomerCompanyId().equals(query.customerCompanyId()))
        .count();

    return new InteractionStatistics(
        totalInteractions,
        recentInteractions,
        positiveInteractions,
        negativeInteractions,
        followUpRequired);
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerInteractionAnalytics getCustomerInteractionAnalytics(GetCustomerInteractionAnalyticsQuery query) {
    log.debug("Fetching customer interaction analytics for: {}", query.customerId());

    CustomerCompanyId customerCompanyId = new CustomerCompanyId(query.customerId());

    List<InteractionType> frequentTypes = interactionRepository
        .findMostFrequentInteractionTypesByCustomer(customerCompanyId);
    FeedbackType predominantFeedback = interactionRepository.findPredominantFeedbackByCustomer(customerCompanyId);
    long totalInteractions = interactionRepository.findByCustomer(customerCompanyId).size();

    // Calculate interaction frequency (interactions per month)
    double monthlyFrequency = calculateMonthlyFrequency(customerCompanyId);

    return new CustomerInteractionAnalytics(
        customerCompanyId.value(),
        frequentTypes,
        predominantFeedback,
        totalInteractions,
        monthlyFrequency);
  }

  private double calculateMonthlyFrequency(CustomerCompanyId customerCompanyId) {
    List<Interaction> customerInteractions = interactionRepository.findByCustomer(customerCompanyId);
    if (customerInteractions.isEmpty()) {
      return 0.0;
    }

    // Find the earliest interaction date
    LocalDateTime earliestDate = customerInteractions.stream()
        .map(interaction -> interaction.getDateTime().value())
        .min(LocalDateTime::compareTo)
        .orElse(LocalDateTime.now());

    long monthsBetween = java.time.temporal.ChronoUnit.MONTHS.between(
        earliestDate.toLocalDate().withDayOfMonth(1),
        LocalDateTime.now().toLocalDate().withDayOfMonth(1));

    if (monthsBetween == 0) {
      monthsBetween = 1; // Avoid division by zero
    }

    return (double) customerInteractions.size() / monthsBetween;
  }
}
