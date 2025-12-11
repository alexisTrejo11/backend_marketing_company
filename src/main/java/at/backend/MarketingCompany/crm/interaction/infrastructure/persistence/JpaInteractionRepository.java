package at.backend.MarketingCompany.crm.interaction.infrastructure.persistence;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaInteractionRepository extends JpaRepository<InteractionEntity, String> {

    Page<InteractionEntity> findByCustomerCompanyId(String customerId, Pageable pageable);

    List<InteractionEntity> findByCustomerCompanyId(String customerId);

    Page<InteractionEntity> findByType(InteractionType type, Pageable pageable);

    Page<InteractionEntity> findByFeedbackType(FeedbackType feedbackType, Pageable pageable);

    Page<InteractionEntity> findByCustomerCompanyIdAndType(String customerId, InteractionType type, Pageable pageable);

    // Time-based queries
    Page<InteractionEntity> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT i FROM InteractionEntity i WHERE i.dateTime >= :startDate")
    List<InteractionEntity> findByDateTimeAfter(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT i FROM InteractionEntity i WHERE i.dateTime >= :cutoffDate")
    Page<InteractionEntity> findRecentInteractions(@Param("cutoffDate") LocalDateTime cutoffDate, Pageable pageable);

    default Page<InteractionEntity> findRecentInteractions(int days, Pageable pageable) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return findRecentInteractions(cutoffDate, pageable);
    }

    @Query("SELECT i FROM InteractionEntity i WHERE i.dateTime >= :startOfDay AND i.dateTime < :endOfDay")
    Page<InteractionEntity> findTodayInteractions(@Param("startOfDay") LocalDateTime startOfDay,
                                                  @Param("endOfDay") LocalDateTime endOfDay,
                                                  Pageable pageable);

    default Page<InteractionEntity> findTodayInteractions(Pageable pageable) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return findTodayInteractions(startOfDay, endOfDay, pageable);
    }

    @Query("SELECT i FROM InteractionEntity i WHERE i.feedbackType IN :followUpTypes")
    Page<InteractionEntity> findInteractionsRequiringFollowUp(@Param("followUpTypes") List<FeedbackType> followUpTypes, Pageable pageable);

    default Page<InteractionEntity> findInteractionsRequiringFollowUp(Pageable pageable) {
        List<FeedbackType> followUpTypes = List.of(
                FeedbackType.NEGATIVE,
                FeedbackType.COMPLAINT,
                FeedbackType.SUGGESTION
        );
        return findInteractionsRequiringFollowUp(followUpTypes, pageable);
    }

    @Query("SELECT i FROM InteractionEntity i WHERE i.feedbackType IN :positiveTypes")
    List<InteractionEntity> findPositiveInteractionsList(@Param("positiveTypes") List<FeedbackType> positiveTypes);

    @Query("SELECT i FROM InteractionEntity i WHERE i.feedbackType IN :positiveTypes")
    Page<InteractionEntity> findPositiveInteractions(@Param("positiveTypes") List<FeedbackType> positiveTypes, Pageable pageable);

    default List<InteractionEntity> findPositiveInteractions() {
        List<FeedbackType> positiveTypes = List.of(FeedbackType.POSITIVE, FeedbackType.COMPLIMENT);
        return findPositiveInteractionsList(positiveTypes);
    }

    @Query("SELECT i FROM InteractionEntity i WHERE i.feedbackType IN :negativeTypes")
    Page<InteractionEntity> findNegativeInteractions(@Param("negativeTypes") List<FeedbackType> negativeTypes, Pageable pageable);

    default Page<InteractionEntity> findNegativeInteractions(Pageable pageable) {
        List<FeedbackType> negativeTypes = List.of(FeedbackType.NEGATIVE, FeedbackType.COMPLAINT);
        return findNegativeInteractions(negativeTypes, pageable);
    }

    // Count queries
    long countByCustomerIdAndType(String customerId, InteractionType type);

    long countByCustomerIdAndFeedbackType(String customerId, FeedbackType feedbackType);

    @Query("SELECT COUNT(i) FROM InteractionEntity i WHERE i.customerId = :customerCompanyId AND i.dateTime >= :startDate")
    long countRecentInteractionsByCustomer(@Param("customerCompanyId") String customerId,
                                           @Param("startDate") LocalDateTime startDate);

    default long countRecentInteractionsByCustomer(String customerId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return countRecentInteractionsByCustomer(customerId, startDate);
    }

    // Analytics - Most frequent interaction types by customer
    @Query("SELECT i.type, COUNT(i) FROM InteractionEntity i WHERE i.customerId = :customerCompanyId GROUP BY i.type ORDER BY COUNT(i) DESC")
    List<Object[]> findInteractionTypeCountsByCustomer(@Param("customerCompanyId") String customerId);

    // Analytics - Predominant feedback type by customer
    @Query("SELECT i.feedbackType, COUNT(i) FROM InteractionEntity i WHERE i.customerId = :customerCompanyId AND i.feedbackType IS NOT NULL GROUP BY i.feedbackType ORDER BY COUNT(i) DESC")
    List<Object[]> findFeedbackTypeCountsByCustomer(@Param("customerCompanyId") String customerId);

    // Search by description or outcome
    @Query("SELECT i FROM InteractionEntity i WHERE " +
            "(LOWER(i.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(i.outcome) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<InteractionEntity> searchByDescriptionOrOutcome(@Param("searchTerm") String searchTerm);

    @Query("SELECT i FROM InteractionEntity i WHERE " +
            "(:customerCompanyId IS NULL OR i.customerId = :customerCompanyId) AND " +
            "(:type IS NULL OR i.type = :type) AND " +
            "(:feedbackType IS NULL OR i.feedbackType = :feedbackType) AND " +
            "(:startDate IS NULL OR i.dateTime >= :startDate) AND " +
            "(:endDate IS NULL OR i.dateTime <= :endDate)")
    List<InteractionEntity> findByCriteria(@Param("customerCompanyId") String customerId,
                                           @Param("type") InteractionType type,
                                           @Param("feedbackType") FeedbackType feedbackType,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT i FROM InteractionEntity i WHERE " +
            "(:customerCompanyId IS NULL OR i.customerId = :customerCompanyId) AND " +
            "(:type IS NULL OR i.type = :type) AND " +
            "(:feedbackType IS NULL OR i.feedbackType = :feedbackType) AND " +
            "(:startDate IS NULL OR i.dateTime >= :startDate) AND " +
            "(:endDate IS NULL OR i.dateTime <= :endDate)")
    Page<InteractionEntity> findByCriteria(@Param("customerCompanyId") String customerId,
                                           @Param("type") InteractionType type,
                                           @Param("feedbackType") FeedbackType feedbackType,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);
}
