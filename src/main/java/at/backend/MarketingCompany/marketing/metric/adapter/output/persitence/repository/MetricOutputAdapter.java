package at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.mapper.MetricEntityMapper;
import at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.model.CampaignMetricEntity;
import at.backend.MarketingCompany.marketing.metric.core.application.query.MetricQuery;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import at.backend.MarketingCompany.marketing.metric.core.port.output.MetricOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MetricOutputAdapter implements MetricOutputPort {
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
	public Page<CampaignMetric> findAll(Pageable pageable) {
		return jpaRepository.findAll(pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignMetric> findByCampaignIdAndIsTargetAchieved(MarketingCampaignId campaignId, Boolean isAchieved, Pageable pageable) {
		return jpaRepository.findByCampaignIdAndTargetAchievement(campaignId.getValue(), isAchieved, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignMetric> findByCampaignIdAndIsAutomated(MarketingCampaignId campaignId, Boolean isAutomated, Pageable pageable) {
		return jpaRepository.findByCampaignIdAndIsAutomated(campaignId.getValue(), isAutomated, pageable)
				.map(mapper::toDomain);
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
	public Page<CampaignMetric> findByFilters(MetricQuery query, Pageable pageable) {
		// no filters, return all
		if (query == null || query.isEmpty()) {
			return findAll(pageable);
		}

		// campaign filter with optional achievement/automation status
		if (query.campaignId() != null) {
			if (query.isTargetAchieved() != null) {
				return jpaRepository.findByCampaignIdAndTargetAchievement(query.campaignId(), query.isTargetAchieved(), pageable)
						.map(mapper::toDomain);
			}
			if (query.isAutomated() != null) {
				return jpaRepository.findByCampaignIdAndIsAutomated(query.campaignId(), query.isAutomated(), pageable)
						.map(mapper::toDomain);
			}
			return jpaRepository.findByCampaignId(query.campaignId(), pageable)
					.map(mapper::toDomain);
		}

		// load a page and filter in-memory for complex combinations
		Page<CampaignMetricEntity> page = jpaRepository.findAll(pageable);
		List<CampaignMetric> filtered = page.stream()
			.filter(e -> {
				// metric types
				if (query.metricTypes() != null && !query.metricTypes().isEmpty()) {
					if (e.getMetricType() == null || !query.metricTypes().contains(e.getMetricType())) return false;
				}

				// target achievement status
				if (query.isTargetAchieved() != null) {
					if (!Objects.equals(Boolean.TRUE.equals(e.getIsTargetAchieved()), query.isTargetAchieved())) return false;
				}

				// automation status
				if (query.isAutomated() != null) {
					if (!Objects.equals(Boolean.TRUE.equals(e.getIsAutomated()), query.isAutomated())) return false;
				}

				// current value range
				if (query.minCurrentValue() != null) {
					if (e.getCurrentValue() == null || e.getCurrentValue().compareTo(query.minCurrentValue()) < 0) return false;
				}
				if (query.maxCurrentValue() != null) {
					if (e.getCurrentValue() == null || e.getCurrentValue().compareTo(query.maxCurrentValue()) > 0) return false;
				}

				// last calculated after
				if (query.lastCalculatedAfter() != null) {
					if (e.getLastCalculatedDate() == null || e.getLastCalculatedDate().isBefore(query.lastCalculatedAfter())) return false;
				}

				// search term - match name
				if (query.searchTerm() != null && !query.searchTerm().isBlank()) {
					String term = query.searchTerm().toLowerCase();
					return e.getName() != null && e.getName().toLowerCase().contains(term);
				}

				return true;
			})
			.map(mapper::toDomain)
			.collect(Collectors.toList());

		return new PageImpl<>(filtered, pageable, filtered.size());
	}

	@Override
	public long countByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged()).getTotalElements();
	}

	@Override
	public BigDecimal calculateAverageValueByCampaignAndMetricType(
			MarketingCampaignId campaignId,
			MetricType metricType) {
		return jpaRepository.calculateAverageValueByCampaignAndMetricType(campaignId.getValue(), metricType);
	}

	@Override
	public BigDecimal calculateAverageAchievementByCampaignId(MarketingCampaignId campaignId) {
		var page = jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged());
		List<CampaignMetricEntity> entities = page.stream().collect(Collectors.toList());
		if (entities.isEmpty()) return BigDecimal.ZERO;

		BigDecimal sum = BigDecimal.ZERO;
		int count = 0;
		for (CampaignMetricEntity e : entities) {
			BigDecimal percent = BigDecimal.ZERO;
			if (e.getTargetValue() != null && e.getTargetValue().compareTo(BigDecimal.ZERO) != 0 && e.getCurrentValue() != null) {
				percent = e.getCurrentValue()
					.divide(e.getTargetValue(), 6, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(100));
			}
			sum = sum.add(percent);
			count++;
		}

		return sum.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP);
	}

	@Override
	public long countAutomatedMetricsByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.findByCampaignIdAndIsAutomated(campaignId.getValue(), true, Pageable.unpaged()).getTotalElements();
	}

	@Override
	public boolean existsByCampaignIdAndName(MarketingCampaignId campaignId, String name) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged())
				.stream()
				.anyMatch(metric -> metric.getName().equalsIgnoreCase(name));
	}

	@Override
	public Map<String, Long> countByMetricTypeByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged())
				.stream()
				.collect(Collectors.groupingBy(e -> {
					if (e.getMetricType() == null) return "UNKNOWN";
					return e.getMetricType().name();
				}, Collectors.counting()));
	}

	@Override
	public Map<String, Long> getPerformanceDistributionByCampaignId(MarketingCampaignId campaignId) {
		List<CampaignMetricEntity> entities = jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged())
				.stream().toList();

		long exceeded = 0, met = 0, near = 0, below = 0, farBelow = 0;
		for (CampaignMetricEntity e : entities) {
			BigDecimal percent = BigDecimal.ZERO;
			if (e.getTargetValue() != null && e.getTargetValue().compareTo(BigDecimal.ZERO) != 0 && e.getCurrentValue() != null) {
				percent = e.getCurrentValue()
					.divide(e.getTargetValue(), 6, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(100));
			}

			if (percent.compareTo(BigDecimal.valueOf(100)) > 0) exceeded++;
			else if (percent.compareTo(BigDecimal.valueOf(100)) == 0) met++;
			else if (percent.compareTo(BigDecimal.valueOf(80)) >= 0) near++;
			else if (percent.compareTo(BigDecimal.valueOf(50)) >= 0) below++;
			else farBelow++;
		}

		return Map.of(
				"EXCEEDED", exceeded,
				"MET", met,
				"NEAR", near,
				"BELOW", below,
				"FAR_BELOW", farBelow
		);
	}
}

