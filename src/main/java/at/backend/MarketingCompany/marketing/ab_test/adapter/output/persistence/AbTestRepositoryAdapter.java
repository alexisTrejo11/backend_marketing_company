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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AbTestRepositoryAdapter implements AbTestRepositoryPort {
	private final AbTestJpaRepository jpaRepository;
	private final AbTestEntityMapper mapper;

	@Override
	@Transactional
	public AbTest save(AbTest abTest) {
		AbTestEntity entity = mapper.toEntity(abTest);
		entity.processNewEntityIfNeeded();

		AbTestEntity savedEntity = jpaRepository.saveAndFlush(entity);
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

	// TODO: Implement more complex filtering in the repository layer if needed
	@Override
	public Page<AbTest> findByFilters(AbTestQuery query, Pageable pageable) {
		// no filters, return all
		if (query == null || query.isEmpty()) {
			return findAll(pageable);
		}

		// campaign filter has a dedicated repository query
		if (query.campaignId() != null) {
			return jpaRepository.findByCampaignId(query.campaignId().getValue(), pageable)
					.map(mapper::toDomain);
		}

		// single test type (if provided)
		if (query.testTypes() != null && query.testTypes().size() == 1) {
			TestType tt = query.testTypes().get(0);
			return jpaRepository.findByTestType(tt, pageable)
					.map(mapper::toDomain);
		}

		// completion status
		if (query.isCompleted() != null) {
			return jpaRepository.findByCompletionStatus(query.isCompleted(), pageable)
					.map(mapper::toDomain);
		}

		// date range
		if (query.startDateFrom() != null && query.startDateTo() != null) {
			return jpaRepository.findByDateRange(query.startDateFrom(), query.startDateTo(), pageable)
					.map(mapper::toDomain);
		}

		// search term
		if (query.searchTerm() != null && !query.searchTerm().isBlank()) {
			return jpaRepository.searchByName(query.searchTerm(), pageable)
					.map(mapper::toDomain);
		}

		// fallback to all
		return findAll(pageable);
	}

	@Override
	public Page<AbTest> findAll(Pageable pageable) {
		return jpaRepository.findAll(pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<AbTest> findScheduledTests(Pageable pageable) {
		// scheduled = startDate in the future and not completed
		Page<AbTestEntity> page = jpaRepository.findAll(pageable);
		List<AbTest> filtered = page.stream()
				.filter(e -> e.getStartDate() != null && e.getStartDate().isAfter(LocalDateTime.now()))
				.filter(e -> e.getIsCompleted() == null || !e.getIsCompleted())
				.map(mapper::toDomain)
				.collect(Collectors.toList());
		return new PageImpl<>(filtered, pageable, filtered.size());
	}

	@Override
	public Page<AbTest> findRunningTests(Pageable pageable) {
		// running = startDate <= now AND (endDate is null OR endDate >= now) AND not completed
		Page<AbTestEntity> page = jpaRepository.findAll(pageable);
		LocalDateTime now = LocalDateTime.now();
		List<AbTest> filtered = page.stream()
				.filter(e -> e.getStartDate() != null && !e.getStartDate().isAfter(now))
				.filter(e -> e.getEndDate() == null || !e.getEndDate().isBefore(now))
				.filter(e -> e.getIsCompleted() == null || !e.getIsCompleted())
				.map(mapper::toDomain)
				.collect(Collectors.toList());
		return new PageImpl<>(filtered, pageable, filtered.size());
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
		List<AbTestEntity> entities = jpaRepository.findCompletedTestsByCampaignId(campaignId.getValue());
		return mapper.toDomainList(entities);
	}

	@Override
	@Transactional(readOnly = true)
	public long countCompletedTestsByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countCompletedTestsByCampaignId(campaignId.getValue());
	}

	@Override
	public long countByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countByCampaignIdAndDeletedAtIsNull(campaignId.getValue());
	}

	@Override
	public List<BigDecimal> getAllConfidenceLevelsByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged())
				.stream()
				.map(AbTestEntity::getConfidenceLevel)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	@Override
	public long countRunningTestsByCampaignId(MarketingCampaignId campaignId) {
		LocalDateTime now = LocalDateTime.now();
		return jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged())
				.stream()
				.filter(e -> e.getStartDate() != null && !e.getStartDate().isAfter(now))
				.filter(e -> e.getEndDate() == null || !e.getEndDate().isBefore(now))
				.filter(e -> e.getIsCompleted() == null || !e.getIsCompleted())
				.count();
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateAverageSignificanceByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.calculateAverageSignificanceByCampaignId(campaignId.getValue());
	}

	@Override
	public Map<String, Long> countByTestTypeByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged())
				.stream()
				.collect(Collectors.groupingBy(e -> {
					if (e.getTestType() == null) return "UNKNOWN";
					return e.getTestType().name();
				}, Collectors.counting()));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByNameAndCampaignId(String testName, MarketingCampaignId campaignId) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged())
				.stream()
				.anyMatch(test -> test.getTestName().equalsIgnoreCase(testName));
	}
}

