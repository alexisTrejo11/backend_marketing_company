package at.backend.MarketingCompany.crm.interaction.infrastructure.persistence;

import at.backend.MarketingCompany.crm.shared.enums.FeedbackType;
import at.backend.MarketingCompany.crm.shared.enums.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaInteractionRepository extends JpaRepository<InteractionEntity, String> {

    // Basic finders
    List<InteractionEntity> findByCustomerModelId(String customerId);
    List<InteractionEntity> findByType(InteractionType type);
    List<InteractionEntity> findByFeedbackType(FeedbackType feedbackType);
    List<InteractionEntity> findByCustomerModelIdAndType(String customerId, InteractionType type);
    
    // Time-based queries
    List<InteractionEntity> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT i FROM InteractionEntity i WHERE i.dateTime >= :startDate")
    List<InteractionEntity> findByDateTimeAfter(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT i FROM InteractionEntity i WHERE i.dateTime >= :cutoffDate")
    List<InteractionEntity> findRecentInteractions(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Default method for recent interactions (last N days)
    default List<InteractionEntity> findRecentInteractions(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return findRecentInteractions(cutoffDate);
    }
    
    // Today's interactions
    @Query("SELECT i FROM InteractionEntity i WHERE i.dateTime >= :startOfDay AND i.dateTime < :endOfDay")
    List<InteractionEntity> findTodayInteractions(@Param("startOfDay") LocalDateTime startOfDay, 
                                                 @Param("endOfDay") LocalDateTime endOfDay);
    
    // Default method for today's interactions
    default List<InteractionEntity> findTodayInteractions() {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return findTodayInteractions(startOfDay, endOfDay);
    }
    
    // Interactions requiring follow-up (negative feedback, complaints, suggestions)
    @Query("SELECT i FROM InteractionEntity i WHERE i.feedbackType IN :followUpTypes")
    List<InteractionEntity> findInteractionsRequiringFollowUp(@Param("followUpTypes") List<FeedbackType> followUpTypes);
    
    // Default method for follow-up interactions
    default List<InteractionEntity> findInteractionsRequiringFollowUp() {
        List<FeedbackType> followUpTypes = List.of(
            FeedbackType.NEGATIVE, 
            FeedbackType.COMPLAINT, 
            FeedbackType.SUGGESTION
        );
        return findInteractionsRequiringFollowUp(followUpTypes);
    }
    
    // Positive interactions
    @Query("SELECT i FROM InteractionEntity i WHERE i.feedbackType IN :positiveTypes")
    List<InteractionEntity> findPositiveInteractions(@Param("positiveTypes") List<FeedbackType> positiveTypes);
    
    // Default method for positive interactions
    default List<InteractionEntity> findPositiveInteractions() {
        List<FeedbackType> positiveTypes = List.of(FeedbackType.POSITIVE, FeedbackType.COMPLIMENT);
        return findPositiveInteractions(positiveTypes);
    }
    
    // Negative interactions
    @Query("SELECT i FROM InteractionEntity i WHERE i.feedbackType IN :negativeTypes")
    List<InteractionEntity> findNegativeInteractions(@Param("negativeTypes") List<FeedbackType> negativeTypes);
    
    // Default method for negative interactions
    default List<InteractionEntity> findNegativeInteractions() {
        List<FeedbackType> negativeTypes = List.of(FeedbackType.NEGATIVE, FeedbackType.COMPLAINT);
        return findNegativeInteractions(negativeTypes);
    }
    
    // Count queries
    long countByCustomerModelIdAndType(String customerId, InteractionType type);
    long countByCustomerModelIdAndFeedbackType(String customerId, FeedbackType feedbackType);
    
    @Query("SELECT COUNT(i) FROM InteractionEntity i WHERE i.customerModel.id = :customerId AND i.dateTime >= :startDate")
    long countRecentInteractionsByCustomer(@Param("customerId") String customerId, 
                                          @Param("startDate") LocalDateTime startDate);
    
    // Default method for count recent interactions
    default long countRecentInteractionsByCustomer(String customerId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return countRecentInteractionsByCustomer(customerId, startDate);
    }
    
    // Analytics - Most frequent interaction types by customer
    @Query("SELECT i.type, COUNT(i) FROM InteractionEntity i WHERE i.customerModel.id = :customerId GROUP BY i.type ORDER BY COUNT(i) DESC")
    List<Object[]> findInteractionTypeCountsByCustomer(@Param("customerId") String customerId);
    
    // Analytics - Predominant feedback type by customer
    @Query("SELECT i.feedbackType, COUNT(i) FROM InteractionEntity i WHERE i.customerModel.id = :customerId AND i.feedbackType IS NOT NULL GROUP BY i.feedbackType ORDER BY COUNT(i) DESC")
    List<Object[]> findFeedbackTypeCountsByCustomer(@Param("customerId") String customerId);
    
    // Search by description or outcome
    @Query("SELECT i FROM InteractionEntity i WHERE " +
           "(LOWER(i.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.outcome) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<InteractionEntity> searchByDescriptionOrOutcome(@Param("searchTerm") String searchTerm);
    
    // Find by multiple criteria
    @Query("SELECT i FROM InteractionEntity i WHERE " +
           "(:customerId IS NULL OR i.customerModel.id = :customerId) AND " +
           "(:type IS NULL OR i.type = :type) AND " +
           "(:feedbackType IS NULL OR i.feedbackType = :feedbackType) AND " +
           "(:startDate IS NULL OR i.dateTime >= :startDate) AND " +
           "(:endDate IS NULL OR i.dateTime <= :endDate)")
    List<InteractionEntity> findByCriteria(@Param("customerId") String customerId,
                                          @Param("type") InteractionType type,
                                          @Param("feedbackType") FeedbackType feedbackType,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
}