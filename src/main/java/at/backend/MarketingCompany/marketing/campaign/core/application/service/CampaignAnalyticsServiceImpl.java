package at.backend.MarketingCompany.marketing.campaign.core.application.service;

import at.backend.MarketingCompany.marketing.campaign.core.application.CampaignPerformanceSummary;
import at.backend.MarketingCompany.marketing.campaign.core.application.CampaignStatistics;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingCampaignNotFoundException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignROI;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.input.CampaignAnalyticsServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CampaignAnalyticsServiceImpl implements CampaignAnalyticsServicePort {
	private final CampaignRepositoryPort campaignRepository;


	@Override
	public CampaignPerformanceSummary getCampaignPerformance(MarketingCampaignId campaignId) {
		MarketingCampaign campaign = findCampaignByIdOrThrow(campaignId);
		CampaignROI roi = campaign.calculateROI();

		return CampaignPerformanceSummary.builder()
				.campaignId(campaign.getId().getValue())
				.campaignName(campaign.getName().value())
				.status(campaign.getStatus())
				.totalBudget(campaign.getBudget().totalBudget())
				.spentAmount(campaign.getBudget().spentAmount())
				.remainingBudget(campaign.getBudget().remainingBudget())
				.budgetUtilizationPercentage(campaign.getBudget().utilizationPercentage())
				.totalAttributedRevenue(campaign.getTotalAttributedRevenue())
				.roiPercentage(roi.roiPercentage())
				.netProfit(roi.netProfit())
				.roiStatus(roi.status().name())
				.isProfitable(campaign.isProfitable())
				.needsOptimization(campaign.needsOptimization())
				.isHighPerformer(campaign.isHighPerformer())
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isCampaignNameAvailable(String name) {
		return !campaignRepository.existsByNameAndNotDeleted(name);
	}

	@Override
	@Transactional(readOnly = true)
	public CampaignStatistics getCampaignStatistics() {
		return CampaignStatistics.builder()
				.totalCampaigns(campaignRepository.countAll())
				.activeCampaigns(campaignRepository.countByStatus(CampaignStatus.ACTIVE))
				.plannedCampaigns(campaignRepository.countByStatus(CampaignStatus.PLANNED))
				.pausedCampaigns(campaignRepository.countByStatus(CampaignStatus.PAUSED))
				.completedCampaigns(campaignRepository.countByStatus(CampaignStatus.COMPLETED))
				.cancelledCampaigns(campaignRepository.countByStatus(CampaignStatus.CANCELLED))
				.totalPlannedBudget(campaignRepository.calculateTotalPlannedBudget())
				.totalActiveSpend(campaignRepository.calculateTotalActiveSpend())
				.totalAttributedRevenue(campaignRepository.calculateTotalAttributedRevenue())
				.overallROI(campaignRepository.calculateOverallROI())
				.campaignsNeedingOptimization(campaignRepository.countCampaignsNeedingOptimization())
				.highPerformingCampaigns(campaignRepository.countHighPerformingCampaigns())
				.build();
	}

	private MarketingCampaign findCampaignByIdOrThrow(MarketingCampaignId campaignId) {
		return campaignRepository.findById(campaignId)
				.orElseThrow(() -> new MarketingCampaignNotFoundException(campaignId));
	}
}
