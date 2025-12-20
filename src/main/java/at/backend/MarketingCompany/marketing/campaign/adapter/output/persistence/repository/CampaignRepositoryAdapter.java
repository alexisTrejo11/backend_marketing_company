package at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.repository;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.mapper.CampaignEntityMapper;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingCampaignNotFoundException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CampaignRepositoryAdapter implements CampaignRepositoryPort {
    private final MarketingCampaignJpaRepository jpaRepository;
    private final CampaignEntityMapper mapper;

    @Override
    @Transactional
    public MarketingCampaign save(MarketingCampaign campaign) {
        MarketingCampaignEntity entity = mapper.toEntity(campaign);
        MarketingCampaignEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarketingCampaign> findById(MarketingCampaignId id) {
        return jpaRepository.findByIdAndNotDeleted(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(MarketingCampaignId id) {
        MarketingCampaignEntity entity = jpaRepository.findByIdAndNotDeleted(id.getValue())
                .orElseThrow(() -> new MarketingCampaignNotFoundException(id));
        entity.setDeletedAt(java.time.LocalDateTime.now());
        jpaRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingCampaign> findByFilters(
            CampaignStatus status,
            CampaignType campaignType,
            Long primaryChannelId, 
            Pageable pageable) {
        return jpaRepository.findByFilters(status, campaignType, primaryChannelId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<MarketingCampaign> findByDateRange(
            LocalDate startDate,
            LocalDate endDate, 
            Pageable pageable) {
        return jpaRepository.findByDateRange(startDate, endDate, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingCampaign> findByBudgetRange(
            BigDecimal minBudget, 
            BigDecimal maxBudget, 
            Pageable pageable) {
        return jpaRepository.findByBudgetRange(minBudget, maxBudget, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingCampaign> searchByName(String searchTerm, Pageable pageable) {
        return jpaRepository.searchByName(searchTerm, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingCampaign> findExpiredActiveCampaigns(Pageable pageable) {
        return jpaRepository.findExpiredActiveCampaigns(pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(CampaignStatus status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPlannedBudget() {
        return jpaRepository.calculateTotalPlannedBudget();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalActiveSpend() {
        return jpaRepository.calculateTotalActiveSpend();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndNotDeleted(String name) {
        return jpaRepository.searchByName(name, Pageable.unpaged())
                .stream()
                .anyMatch(campaign -> campaign.getName().equalsIgnoreCase(name));
    }
}