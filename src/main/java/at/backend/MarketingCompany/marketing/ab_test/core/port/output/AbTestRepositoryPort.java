package at.backend.MarketingCompany.marketing.ab_test.core.port.output;

import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AbTestRepositoryPort {

	AbTest save(AbTest abTest);

	Optional<AbTest> findById(AbTestId id);

	void delete(AbTestId id);

	Page<AbTest> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);

	Page<AbTest> findByFilters(AbTestQuery query, Pageable pageable);

	Page<AbTest> findAll(Pageable pageable);

	Page<AbTest> findScheduledTests(Pageable pageable);

	Page<AbTest> findRunningTests(Pageable pageable);

	Page<AbTest> findByCompletionStatus(Boolean isCompleted, Pageable pageable);

	Page<AbTest> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	Page<AbTest> searchByName(String searchTerm, Pageable pageable);

	List<AbTest> findCompletedTestsByCampaignId(MarketingCampaignId campaignId);

	long countCompletedTestsByCampaignId(MarketingCampaignId campaignId);

	long countByCampaignId(MarketingCampaignId campaignId);

	List<BigDecimal> getAllConfidenceLevelsByCampaignId(MarketingCampaignId campaignId);

	long countRunningTestsByCampaignId(MarketingCampaignId campaignId);

	BigDecimal calculateAverageSignificanceByCampaignId(MarketingCampaignId campaignId);

	Map<String, Long> countByTestTypeByCampaignId(MarketingCampaignId campaignId);

	boolean existsByNameAndCampaignId(String testName, MarketingCampaignId campaignId);
}
