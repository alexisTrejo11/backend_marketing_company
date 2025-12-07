package at.backend.MarketingCompany.crm.interaction.infrastructure.persistence;

import at.backend.MarketingCompany.crm.interaction.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import at.backend.MarketingCompany.crm.interaction.domain.repository.InteractionRepository;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InteractionRepositoryImpl implements InteractionRepository {

  private final JpaInteractionRepository jpaInteractionRepository;
  private final InteractionEntityMapper interactionEntityMapper;

  @Override
  @Transactional
  public Interaction save(Interaction interaction) {
    log.debug("Saving interaction with ID: {}", interaction.getId().value());

    InteractionEntity entity = interactionEntityMapper.toEntity(interaction);
    InteractionEntity savedEntity = jpaInteractionRepository.save(entity);

    log.info("Interaction saved successfully with ID: {}", savedEntity.getId());
    return interactionEntityMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Interaction> findById(InteractionId interactionId) {
    log.debug("Finding interaction by ID: {}", interactionId.value());

    return jpaInteractionRepository.findById(interactionId.value())
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  @Transactional
  public void delete(Interaction interaction) {
    log.debug("Deleting interaction with ID: {}", interaction.getId().value());

    InteractionEntity entity = interactionEntityMapper.toEntity(interaction);
    jpaInteractionRepository.delete(entity);

    log.info("Interaction deleted successfully with ID: {}", interaction.getId().value());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(InteractionId interactionId) {
    return jpaInteractionRepository.existsById(interactionId.value());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> findByCustomer(CustomerId customerId, Pageable pageable) {
    log.debug("Finding paginated interactions for customer: {}", customerId.value());

    return jpaInteractionRepository.findByCustomerId(customerId.value(), pageable)
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  public List<Interaction> findByCustomer(CustomerId customerId) {
    log.debug("Finding interactions for customer: {}", customerId.value());

    return jpaInteractionRepository.findByCustomerId(customerId.value()).stream()
        .map(interactionEntityMapper::toDomain)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> findByType(InteractionType type, Pageable pageable) {
    log.debug("Finding interactions by type: {}", type);

    return jpaInteractionRepository.findByType(type, pageable)
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> findByFeedbackType(FeedbackType feedbackType, Pageable pageable) {
    log.debug("Finding interactions by feedback type: {}", feedbackType);

    return jpaInteractionRepository.findByFeedbackType(feedbackType, pageable)
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> findByCustomerAndType(CustomerId customerId, InteractionType type, Pageable pageable) {
    log.debug("Finding interactions for customer {} and type: {}", customerId.value(), type);

    return jpaInteractionRepository.findByCustomerIdAndType(customerId.value(), type, pageable)
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
    log.debug("Finding interactions between {} and {}", startDate, endDate);

    return jpaInteractionRepository.findByDateTimeBetween(startDate, endDate, pageable)
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> findRecentInteractions(int days, Pageable pageable) {
    log.debug("Finding recent interactions from last {} days", days);

    return jpaInteractionRepository.findRecentInteractions(days, pageable)
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> findTodayInteractions(Pageable pageable) {
    log.debug("Finding today's interactions");

    return jpaInteractionRepository.findTodayInteractions(pageable)
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> findInteractionsRequiringFollowUp(Pageable pageable) {
    log.debug("Finding interactions requiring follow-up");

    return jpaInteractionRepository.findInteractionsRequiringFollowUp(pageable)
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  public List<Interaction> findInteractionsRequiringFollowUp() {
    return List.of();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Interaction> findPositiveInteractions(Pageable pageable) {
    log.debug("Finding positive interactions");

    var positiveFeedbackTypes = FeedbackType.getPositiveFeedbackTypes();

    return jpaInteractionRepository.findPositiveInteractions(positiveFeedbackTypes, pageable)
        .map(interactionEntityMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByCustomerAndType(CustomerId customerId, InteractionType type) {
    log.debug("Counting interactions for customer {} and type: {}", customerId.value(), type);

    return jpaInteractionRepository.countByCustomerIdAndType(customerId.value(), type);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByCustomerAndFeedbackType(CustomerId customerId, FeedbackType feedbackType) {
    log.debug("Counting interactions for customer {} and feedback type: {}", customerId.value(), feedbackType);

    return jpaInteractionRepository.countByCustomerIdAndFeedbackType(customerId.value(), feedbackType);
  }

  @Override
  @Transactional(readOnly = true)
  public long countRecentInteractionsByCustomer(CustomerId customerId, int days) {
    log.debug("Counting recent interactions for customer {} from last {} days", customerId.value(), days);

    return jpaInteractionRepository.countRecentInteractionsByCustomer(customerId.value(), days);
  }

  @Override
  @Transactional(readOnly = true)
  public List<InteractionType> findMostFrequentInteractionTypesByCustomer(CustomerId customerId) {
    log.debug("Finding most frequent interaction types for customer: {}", customerId.value());

    List<Object[]> results = jpaInteractionRepository.findInteractionTypeCountsByCustomer(customerId.value());

    return results.stream()
        .map(result -> (InteractionType) result[0])
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public FeedbackType findPredominantFeedbackByCustomer(CustomerId customerId) {
    log.debug("Finding predominant feedback type for customer: {}", customerId.value());

    List<Object[]> results = jpaInteractionRepository.findFeedbackTypeCountsByCustomer(customerId.value());

    if (results.isEmpty()) {
      return null;
    }

    return (FeedbackType) results.getFirst()[0]; // Return the most frequent feedback type
  }

  @Override
  @Transactional(readOnly = true)
  public List<Interaction> searchInteractions(String searchTerm) {
    log.debug("Searching interactions with term: {}", searchTerm);

    if (searchTerm == null || searchTerm.trim().isEmpty()) {
      return List.of();
    }

    return jpaInteractionRepository.searchByDescriptionOrOutcome(searchTerm.trim()).stream()
        .map(interactionEntityMapper::toDomain)
        .toList();
  }

  @Override
  public Page<Interaction> findNegativeInteractions(Pageable pageable) {
    Page<InteractionEntity> entityPage = jpaInteractionRepository.findNegativeInteractions(pageable);
    return entityPage.map(interactionEntityMapper::toDomain);
  }

  @Transactional(readOnly = true)
  public List<Interaction> findByCriteria(String customerId, InteractionType type,
      FeedbackType feedbackType, LocalDateTime startDate,
      LocalDateTime endDate) {
    log.debug("Finding interactions with criteria - customer: {}, type: {}, feedback: {}",
        customerId, type, feedbackType);

    return jpaInteractionRepository.findByCriteria(customerId, type, feedbackType, startDate, endDate).stream()
        .map(interactionEntityMapper::toDomain)
        .toList();
  }
}
