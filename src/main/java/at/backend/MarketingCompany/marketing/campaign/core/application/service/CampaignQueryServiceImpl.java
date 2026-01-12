package at.backend.MarketingCompany.marketing.campaign.core.application.service;

import at.backend.MarketingCompany.marketing.campaign.core.application.query.CampaignQuery;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingCampaignNotFoundException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.input.CampaignQueryServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CampaignQueryServiceImpl implements CampaignQueryServicePort {
	private final CampaignRepositoryPort campaignRepository;

	@Override
	@Transactional(readOnly = true)
	public MarketingCampaign getCampaignById(MarketingCampaignId campaignId) {
		return findCampaignByIdOrThrow(campaignId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingCampaign> getAllCampaigns(Pageable pageable) {
		return campaignRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingCampaign> searchCampaigns(CampaignQuery query, Pageable pageable) {
		if (query.isEmpty()) {
			return getAllCampaigns(pageable);
		}

		return null; // TODO: Implement complex query handling
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingCampaign> getCampaignsByStatus(
			CampaignStatus status,
			Pageable pageable) {

		return campaignRepository.findByStatus(status, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingCampaign> getExpiredActiveCampaigns(Pageable pageable) {
		return campaignRepository.findExpiredActiveCampaigns(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingCampaign> getCampaignsNeedingOptimization(Pageable pageable) {
		return campaignRepository.findCampaignsNeedingOptimization(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MarketingCampaign> getHighPerformingCampaigns(Pageable pageable) {
		return campaignRepository.findHighPerformingCampaigns(pageable);
	}

	private MarketingCampaign findCampaignByIdOrThrow(MarketingCampaignId campaignId) {
		return campaignRepository.findById(campaignId)
				.orElseThrow(() -> new MarketingCampaignNotFoundException(campaignId));
	}
}
