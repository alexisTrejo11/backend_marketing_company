package at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.repository;

import at.backend.MarketingCompany.marketing.campaign.core.domain.models.AbTest;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AbTestRepositoryPort {

	AbTest save(AbTest abTest);

	Optional<AbTest> findById(Long id);

	void delete(Long id);

	Page<AbTest> findByCampaignId(Long campaignId, Pageable pageable);

	Page<AbTest> findByTestType(TestType testType, Pageable pageable);

	Page<AbTest> findByCompletionStatus(Boolean isCompleted, Pageable pageable);

	Page<AbTest> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	Page<AbTest> searchByName(String searchTerm, Pageable pageable);

	long countCompletedTestsByCampaignId(Long campaignId);

	BigDecimal calculateAverageSignificanceByCampaignId(Long campaignId);

	boolean existsByNameAndCampaignIdAndNotDeleted(String testName, Long campaignId);
}
