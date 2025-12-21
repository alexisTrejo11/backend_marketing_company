package at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.repository;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.mapper.ActivityEntityMapper;
import at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.model.CampaignActivityEntity;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.activity.core.port.output.ActivityRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
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
public class ActivityRepositoryAdapter implements ActivityRepositoryPort {

	private final CampaignActivityJpaRepository jpaRepository;
	private final ActivityEntityMapper mapper;

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
	public Page<CampaignActivity> findByPlannedDateRange(
			LocalDateTime startDate,
			LocalDateTime endDate,
			Pageable pageable) {
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