package at.backend.MarketingCompany.marketing.ab_test.adapter.output.persistence;

import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.ab_test.core.port.output.AbTestRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.AbTestEntity;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.repository.AbTestJpaRepository;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AbTestRepositoryAdapter implements AbTestRepositoryPort {
    private final AbTestJpaRepository jpaRepository;
    private final AbTestEntityMapper mapper;

    @Override
    @Transactional
    public AbTest save(AbTest abTest) {
        AbTestEntity entity = mapper.toEntity(abTest);
        AbTestEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AbTest> findById(AbTestId id) {
        return jpaRepository.findByIdAndNotDeleted(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(AbTestId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AbTest> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable) {
        return jpaRepository.findByCampaignId(campaignId.getValue(), pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<AbTest> findByFilters(AbTestQuery query, Pageable pageable) {
        return null;
    }

    @Override
    public Page<AbTest> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<AbTest> findScheduledTests(Pageable pageable) {
        return null;
    }

    @Override
    public Page<AbTest> findRunningTests(Pageable pageable) {
        return null;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<AbTest> findByCompletionStatus(Boolean isCompleted, Pageable pageable) {
        return jpaRepository.findByCompletionStatus(isCompleted, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AbTest> findByDateRange(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Pageable pageable) {
        return jpaRepository.findByDateRange(startDate, endDate, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AbTest> searchByName(String searchTerm, Pageable pageable) {
        return jpaRepository.searchByName(searchTerm, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<AbTest> findCompletedTestsByCampaignId(MarketingCampaignId campaignId) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public long countCompletedTestsByCampaignId(MarketingCampaignId campaignId) {
        return jpaRepository.countCompletedTestsByCampaignId(campaignId.getValue());
    }

    @Override
    public long countByCampaignId(MarketingCampaignId campaignId) {
        return 0;
    }

    @Override
    public List<BigDecimal> getAllConfidenceLevelsByCampaignId(MarketingCampaignId campaignId) {
        return List.of();
    }

    @Override
    public long countRunningTestsByCampaignId(MarketingCampaignId campaignId) {
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateAverageSignificanceByCampaignId(MarketingCampaignId campaignId) {
        return jpaRepository.calculateAverageSignificanceByCampaignId(campaignId.getValue());
    }

    @Override
    public Map<String, Long> countByTestTypeByCampaignId(MarketingCampaignId campaignId) {
        return Map.of();
    }

    @Transactional(readOnly = true)
    public boolean existsByNameAndCampaignId(String testName, MarketingCampaignId campaignId) {
        return jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged())
                .stream()
                .anyMatch(test -> test.getTestName().equalsIgnoreCase(testName));
    }
}