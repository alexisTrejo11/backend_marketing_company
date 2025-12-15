package at.backend.MarketingCompany.crm.interaction.core.ports.output;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.*;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
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
  Page<Interaction> findByCustomer(CustomerCompanyId customerCompanyId, Pageable pageable);

  List<Interaction> findByCustomer(CustomerCompanyId customerCompanyId);

  Page<Interaction> findByType(InteractionType type, Pageable pageable);

  Page<Interaction> findByFeedbackType(FeedbackType feedbackType, Pageable pageable);

  Page<Interaction> findByCustomerAndType(CustomerCompanyId customerCompanyId, InteractionType type, Pageable pageable);

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
  long countByCustomerAndType(CustomerCompanyId customerCompanyId, InteractionType type);

  long countByCustomerAndFeedbackType(CustomerCompanyId customerCompanyId, FeedbackType feedbackType);

  long countRecentInteractionsByCustomer(CustomerCompanyId customerCompanyId, int days);

  // Analytics
  List<InteractionType> findMostFrequentInteractionTypesByCustomer(CustomerCompanyId customerCompanyId);

  FeedbackType findPredominantFeedbackByCustomer(CustomerCompanyId customerCompanyId);
}
