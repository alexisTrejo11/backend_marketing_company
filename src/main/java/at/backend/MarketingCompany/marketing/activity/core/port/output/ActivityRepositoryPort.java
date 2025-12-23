package at.backend.MarketingCompany.marketing.activity.core.port.output;


import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityStatus;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ActivityRepositoryPort {

	CampaignActivity save(CampaignActivity activity);

	Optional<CampaignActivity> findById(CampaignActivityId id);

	void delete(CampaignActivityId id);

	Page<CampaignActivity> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignActivity> findByCampaignIdAndStatus(MarketingCampaignId campaignId, ActivityStatus status, Pageable pageable);

	Page<CampaignActivity> findByAssignedUserId(UserId userId, Pageable pageable);

	Page<CampaignActivity> findByPlannedDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	Page<CampaignActivity> findUpcomingActivities(LocalDateTime date, Pageable pageable);

	long countByCampaignIdAndStatus(MarketingCampaignId campaignId, ActivityStatus status);

	BigDecimal calculateTotalActualCostByCampaignId(MarketingCampaignId campaignId);

	BigDecimal calculateAverageCostOverrunByCampaignId(MarketingCampaignId campaignId);
}