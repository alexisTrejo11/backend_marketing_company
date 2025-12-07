package at.backend.MarketingCompany.crm.interaction.domain.repository;

import at.backend.MarketingCompany.crm.interaction.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InteractionRepository {
  // Basic CRUD
  Interaction save(Interaction interaction);

  Optional<Interaction> findById(InteractionId interactionId);

  void delete(Interaction interaction);

  boolean existsById(InteractionId interactionId);

  // Finders
  Page<Interaction> findByCustomer(CustomerId customerId, Pageable pageable);

  List<Interaction> findByCustomer(CustomerId customerId);

  Page<Interaction> findByType(InteractionType type, Pageable pageable);

  Page<Interaction> findByFeedbackType(FeedbackType feedbackType, Pageable pageable);

  Page<Interaction> findByCustomerAndType(CustomerId customerId, InteractionType type, Pageable pageable);

  // Time-based queries
  Page<Interaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

  Page<Interaction> findRecentInteractions(int days, Pageable pageable);

  Page<Interaction> findTodayInteractions(Pageable pageable);

  // Special queries
  List<Interaction> searchInteractions(String searchTerm);

  Page<Interaction> findInteractionsRequiringFollowUp(Pageable pageable);

  List<Interaction> findInteractionsRequiringFollowUp();

  Page<Interaction> findPositiveInteractions(Pageable pageable);

  Page<Interaction> findNegativeInteractions(Pageable pageable);

  // Statistics
  long countByCustomerAndType(CustomerId customerId, InteractionType type);

  long countByCustomerAndFeedbackType(CustomerId customerId, FeedbackType feedbackType);

  long countRecentInteractionsByCustomer(CustomerId customerId, int days);

  // Analytics
  List<InteractionType> findMostFrequentInteractionTypesByCustomer(CustomerId customerId);

  FeedbackType findPredominantFeedbackByCustomer(CustomerId customerId);
}
