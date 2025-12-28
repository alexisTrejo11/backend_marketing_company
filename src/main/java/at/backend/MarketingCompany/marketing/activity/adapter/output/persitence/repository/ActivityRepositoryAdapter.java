package at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.repository;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.mapper.ActivityEntityMapper;
import at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.model.CampaignActivityEntity;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityType;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.activity.core.port.output.ActivityRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ActivityRepositoryAdapter implements ActivityRepositoryPort {
	private final CampaignActivityJpaRepository jpaRepository;
	private final ActivityEntityMapper mapper;

	@Override
	public Page<CampaignActivity> findAll(Pageable pageable) {
		return jpaRepository.findAll(pageable).map(mapper::toDomain);
	}

	@Override
	public Page<CampaignActivity> findByFilters(Long campaignId, List<ActivityStatus> statuses, List<ActivityType> activityTypes, Long assignedToUserId, LocalDateTime plannedStartFrom, LocalDateTime plannedStartTo, Boolean isDelayed, Boolean isCompleted, String searchTerm, Pageable pageable) {
		Page<CampaignActivityEntity> initialPage = (campaignId != null)
				? jpaRepository.findByCampaignId(campaignId, Pageable.unpaged())
				: jpaRepository.findAll(Pageable.unpaged());

		List<CampaignActivityEntity> entities = initialPage.getContent().stream()
			.filter(e -> e.getDeletedAt() == null)
			.filter(e -> {
				if (statuses != null && !statuses.isEmpty()) {
					return statuses.contains(e.getStatus());
				}
				return true;
			})
			.filter(e -> {
				if (activityTypes != null && !activityTypes.isEmpty()) {
					return activityTypes.contains(e.getActivityType());
				}
				return true;
			})
			.filter(e -> {
				if (assignedToUserId != null) {
					return e.getAssignedToUserId() != null && e.getAssignedToUserId().equals(assignedToUserId);
				}
				return true;
			})
			.filter(e -> {
				if (plannedStartFrom != null) {
					if (e.getPlannedStartDate() == null) return false;
					return !e.getPlannedStartDate().isBefore(plannedStartFrom);
				}
				return true;
			})
			.filter(e -> {
				if (plannedStartTo != null) {
					if (e.getPlannedEndDate() == null) return false;
					return !e.getPlannedEndDate().isAfter(plannedStartTo);
				}
				return true;
			})
			.filter(e -> {
				if (isDelayed != null) {
					boolean delayed = false;
					if (e.getPlannedEndDate() != null) {
						LocalDateTime now = LocalDateTime.now();
						delayed = e.getPlannedEndDate().isBefore(now)
							&& e.getStatus() != ActivityStatus.COMPLETED
							&& e.getStatus() != ActivityStatus.CANCELLED;
					}
					return isDelayed.equals(delayed);
				}
				return true;
			})
			.filter(e -> {
				if (isCompleted != null) {
					boolean completed = e.getStatus() == ActivityStatus.COMPLETED;
					return isCompleted.equals(completed);
				}
				return true;
			})
			.filter(e -> {
				if (searchTerm != null && !searchTerm.isBlank()) {
					String lower = searchTerm.toLowerCase();
					String name = e.getName() != null ? e.getName().toLowerCase() : "";
					String desc = e.getDescription() != null ? e.getDescription().toLowerCase() : "";
					return name.contains(lower) || desc.contains(lower);
				}
				return true;
			})
			.toList();

		List<CampaignActivity> domainList = entities.stream()
			.map(mapper::toDomain)
			.collect(Collectors.toList());

		if (pageable == null || pageable.isUnpaged()) {
			return new PageImpl<>(domainList);
		}

		int total = domainList.size();
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), total);
		List<CampaignActivity> content = new ArrayList<>();
		if (start <= end && start < total) {
			content = domainList.subList(start, end);
		}
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	@Transactional
	public CampaignActivity save(CampaignActivity activity) {
		CampaignActivityEntity entity = mapper.toEntity(activity);
		CampaignActivityEntity savedEntity = jpaRepository.save(entity);
		return mapper.toDomain(savedEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CampaignActivity> findById(CampaignActivityId id) {
		return jpaRepository.findByIdAndNotDeleted(id.getValue())
				.map(mapper::toDomain);
	}

	@Override
	@Transactional
	public void delete(CampaignActivityId id) {
		jpaRepository.deleteById(id.getValue());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignActivity> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignActivity> findDelayedActivities(Pageable pageable) {
		Page<CampaignActivityEntity> all = jpaRepository.findAll(Pageable.unpaged());
		List<CampaignActivity> delayed = all.getContent().stream()
			.filter(e -> e.getDeletedAt() == null)
			.filter(e -> {
				if (e.getPlannedEndDate() == null) return false;
				LocalDateTime now = LocalDateTime.now();
				return e.getPlannedEndDate().isBefore(now)
					&& e.getStatus() != ActivityStatus.COMPLETED
					&& e.getStatus() != ActivityStatus.CANCELLED;
			})
			.map(mapper::toDomain)
			.collect(Collectors.toList());

		if (pageable == null || pageable.isUnpaged()) {
			return new PageImpl<>(delayed);
		}

		int total = delayed.size();
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), total);
		List<CampaignActivity> content = new ArrayList<>();
		if (start <= end && start < total) {
			content = delayed.subList(start, end);
		}
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<CampaignActivity> findOverBudgetActivities(Pageable pageable) {
		Page<CampaignActivityEntity> all = jpaRepository.findAll(Pageable.unpaged());
		List<CampaignActivity> over = all.getContent().stream()
			.filter(e -> e.getDeletedAt() == null)
			.filter(e -> {
				BigDecimal planned = e.getPlannedCost();
				BigDecimal actual = e.getActualCost();
				BigDecimal overrun = e.getCostOverrunPercentage();
				if (overrun != null && overrun.compareTo(BigDecimal.ZERO) > 0) return true;
				if (planned != null && actual != null) {
					return actual.compareTo(planned) > 0;
				}
				return false;
			})
			.map(mapper::toDomain)
			.collect(Collectors.toList());

		if (pageable == null || pageable.isUnpaged()) {
			return new PageImpl<>(over);
		}

		int total = over.size();
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), total);
		List<CampaignActivity> content = new ArrayList<>();
		if (start <= end && start < total) {
			content = over.subList(start, end);
		}
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignActivity> findByCampaignIdAndStatus(
			MarketingCampaignId campaignId,
			ActivityStatus status,
			Pageable pageable) {
		return jpaRepository.findByCampaignIdAndStatus(campaignId.getValue(), status, pageable)
				.map(mapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignActivity> findByAssignedUserId(UserId userId, Pageable pageable) {
		return jpaRepository.findByAssignedUserId(userId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignActivity> findByPlannedDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
		return jpaRepository.findByPlannedDateRange(startDate, endDate, pageable)
				.map(mapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignActivity> findUpcomingActivities(LocalDateTime date, Pageable pageable) {
		return jpaRepository.findUpcomingActivities(date, pageable)
				.map(mapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public long countByCampaignIdAndStatus(MarketingCampaignId campaignId, ActivityStatus status) {
		return jpaRepository.countByCampaignIdAndStatus(campaignId.getValue(), status);
	}

	@Override
	public Long countDelayedActivitiesByCampaignId(MarketingCampaignId campaignId) {
		Page<CampaignActivityEntity> page = jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged());
		return page.getContent().stream()
			.filter(e -> e.getDeletedAt() == null)
			.filter(e -> {
				if (e.getPlannedEndDate() == null) return false;
				LocalDateTime now = LocalDateTime.now();
				return e.getPlannedEndDate().isBefore(now)
					&& e.getStatus() != ActivityStatus.COMPLETED
					&& e.getStatus() != ActivityStatus.CANCELLED;
			})
			.count();
	}

	@Override
	public Double calculateOnTimeCompletionRateByCampaignId(MarketingCampaignId campaignId) {
		Page<CampaignActivityEntity> page = jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged());
		List<CampaignActivityEntity> entities = page.getContent().stream()
			.filter(e -> e.getDeletedAt() == null)
			.toList();

		long completed = entities.stream()
			.filter(e -> e.getStatus() == ActivityStatus.COMPLETED)
			.count();
		if (completed == 0) return 0.0;

		long onTime = entities.stream()
			.filter(e -> e.getStatus() == ActivityStatus.COMPLETED)
			.filter(e -> {
				if (e.getActualEndDate() == null || e.getPlannedEndDate() == null) return false;
				return !e.getActualEndDate().isAfter(e.getPlannedEndDate());
			})
			.count();

		return onTime / (double) completed;
	}

	@Override
	public long countByCampaignId(MarketingCampaignId campaignId) {
		Page<CampaignActivityEntity> page = jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged());
		return page.getContent().stream().filter(e -> e.getDeletedAt() == null).count();
	}

	@Override
	public BigDecimal calculateTotalPlannedCostByCampaignId(MarketingCampaignId campaignId) {
		Page<CampaignActivityEntity> page = jpaRepository.findByCampaignId(campaignId.getValue(), Pageable.unpaged());
		return page.getContent().stream()
			.filter(e -> e.getDeletedAt() == null)
			.map(CampaignActivityEntity::getPlannedCost)
			.filter(Objects::nonNull)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateTotalActualCostByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.calculateTotalActualCostByCampaignId(campaignId.getValue());
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateAverageCostOverrunByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.calculateAverageCostOverrunByCampaignId(campaignId.getValue());
	}
}

