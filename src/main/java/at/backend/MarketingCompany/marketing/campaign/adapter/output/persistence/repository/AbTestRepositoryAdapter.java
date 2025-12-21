package at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.repository;


import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.AbTestEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.AbTest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public Optional<AbTest> findById(Long id) {
        return jpaRepository.findByIdAndNotDeleted(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        AbTestEntity entity = jpaRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("AB Test not found with id: " + id));
        entity.setDeletedAt(java.time.LocalDateTime.now());
        jpaRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AbTest> findByCampaignId(Long campaignId, Pageable pageable) {
        return jpaRepository.findByCampaignId(campaignId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AbTest> findByTestType(
            AbTestEntity.TestType testType, 
            Pageable pageable) {
        return jpaRepository.findByTestType(testType, pageable)
                .map(mapper::toDomain);
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
    @Transactional(readOnly = true)
    public long countCompletedTestsByCampaignId(Long campaignId) {
        return jpaRepository.countCompletedTestsByCampaignId(campaignId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateAverageSignificanceByCampaignId(Long campaignId) {
        return jpaRepository.calculateAverageSignificanceByCampaignId(campaignId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndCampaignIdAndNotDeleted(String testName, Long campaignId) {
        return jpaRepository.findByCampaignId(campaignId, Pageable.unpaged())
                .stream()
                .anyMatch(test -> test.getTestName().equalsIgnoreCase(testName));
    }
}