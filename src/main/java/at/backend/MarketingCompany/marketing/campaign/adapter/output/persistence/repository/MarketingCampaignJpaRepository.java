package at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.repository;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MarketingCampaignJpaRepository extends JpaRepository<MarketingCampaignEntity, Long> {

	@Query("SELECT c FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL AND c.id = :id")
	Optional<MarketingCampaignEntity> findByIdAndNotDeleted(@Param("id") Long id);

	@Query("SELECT c FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL " +
			"AND (:status IS NULL OR c.status = :status) " +
			"AND (:campaignType IS NULL OR c.campaignType = :campaignType) " +
			"AND (:primaryChannelId IS NULL OR c.primaryChannel.id = :primaryChannelId)")
	Page<MarketingCampaignEntity> findByFilters(
			@Param("status") CampaignStatus status,
			@Param("campaignType") CampaignType campaignType,
			@Param("primaryChannelId") Long primaryChannelId,
			Pageable pageable);

	@Query("SELECT c FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL " +
			"AND c.startDate >= :startDate AND c.endDate <= :endDate")
	Page<MarketingCampaignEntity> findByDateRange(
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,
			Pageable pageable);

	@Query("SELECT c FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL " +
			"AND c.totalBudget >= :minBudget AND c.totalBudget <= :maxBudget")
	Page<MarketingCampaignEntity> findByBudgetRange(
			@Param("minBudget") BigDecimal minBudget,
			@Param("maxBudget") BigDecimal maxBudget,
			Pageable pageable);

	@Query("SELECT c FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL " +
			"AND LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	Page<MarketingCampaignEntity> searchByName(
			@Param("searchTerm") String searchTerm,
			Pageable pageable);


	@Query("SELECT c FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL AND c.status = :status")
	Page<MarketingCampaignEntity> findByStatus(@Param("status") CampaignStatus status, Pageable pageable);

	@Override
	Page<MarketingCampaignEntity> findAll(Pageable pageable);

	@Query("SELECT c FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL " +
			"AND c.status = 'ACTIVE' AND c.endDate IS NOT NULL AND c.endDate < CURRENT_DATE")
	Page<MarketingCampaignEntity> findExpiredActiveCampaigns(Pageable pageable);

	@Query("SELECT COUNT(c) FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL AND c.status = :status")
	long countByStatus(@Param("status") CampaignStatus status);

	@Query("SELECT COUNT(c) FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL")
	long countNotDeleted();

	@Query("SELECT SUM(c.totalBudget) FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL " +
			"AND c.status IN ('ACTIVE', 'PLANNED')")
	BigDecimal calculateTotalPlannedBudget();

	@Query("SELECT SUM(c.spentAmount) FROM MarketingCampaignEntity c WHERE c.deletedAt IS NULL " +
			"AND c.status = 'ACTIVE'")
	BigDecimal calculateTotalActiveSpend();
}