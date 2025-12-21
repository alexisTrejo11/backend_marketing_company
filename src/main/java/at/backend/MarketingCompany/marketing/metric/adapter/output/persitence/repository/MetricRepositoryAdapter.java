package at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.mapper.MetricEntityMapper;
import at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.model.CampaignMetricEntity;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import at.backend.MarketingCompany.marketing.metric.core.port.output.MetricRepositoryPort;
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
public class MetricRepositoryAdapter implements MetricRepositoryPort {
	private final CampaignMetricJpaRepository jpaRepository;
	private final MetricEntityMapper mapper;

	@Override
	@Transactional
	public CampaignMetric save(CampaignMetric metric) {
		CampaignMetricEntity entity = mapper.toEntity(metric);
		CampaignMetricEntity savedEntity = jpaRepository.save(entity);
		return mapper.toDomain(savedEntity);
	}

	@Override
	
	public Optional<CampaignMetric> findById(CampaignMetricId id) {
		return jpaRepository.findByIdAndNotDeleted(id.getValue())
				.map(mapper::toDomain);
	}

	@Override
	@Transactional
	public void delete(CampaignMetricId id) {
		jpaRepository.deleteById(id.getValue());
	}

	@Override
	
	public Page<CampaignMetric> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	
	public Page<CampaignMetric> findByCampaignIdAndMetricType(
			MarketingCampaignId campaignId,
			MetricType metricType,
			Pageable pageable) {
		return jpaRepository.findByCampaignIdAndMetricType(campaignId.getValue(), metricType, pageable)
				.map(mapper::toDomain);
	}

	@Override
	
	public Page<CampaignMetric> findByAutomationStatus(Boolean isAutomated, Pageable pageable) {
		return jpaRepository.findByAutomationStatus(isAutomated, pageable)
				.map(mapper::toDomain);
	}

	@Override
	
	public Page<CampaignMetric> findRecentlyUpdated(LocalDateTime fromDate, Pageable pageable) {
		return jpaRepository.findRecentlyUpdated(fromDate, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignMetric> findByCampaignIdAndTargetAchievement(
			MarketingCampaignId campaignId,
			Boolean isAchieved,
			Pageable pageable) {
		return jpaRepository.findByCampaignIdAndTargetAchievement(campaignId.getValue(), isAchieved, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public long countAchievedMetricsByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countAchievedMetricsByCampaignId(campaignId.getValue());
	}

	@Override
	public BigDecimal calculateAverageValueByCampaignAndMetricType(
			MarketingCampaignId campaignId,
			MetricType metricType) {
		return jpaRepository.calculateAverageValueByCampaignAndMetricType(campaignId.getValue(), metricType);
	}

	@Override
	public boolean existsByCampaignIdAndNameAndNotDeleted(MarketingCampaignId campaignId, String name) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged())
				.stream()
				.anyMatch(metric -> metric.getName().equalsIgnoreCase(name));
	}
}