package at.backend.MarketingCompany.marketing.ab_test.core.application.service;

import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.ab_test.core.port.input.AbTestQueryServicePort;
import at.backend.MarketingCompany.marketing.ab_test.core.port.output.AbTestRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AbTestQueryService implements AbTestQueryServicePort {
	private final AbTestRepositoryPort abTestRepository;
	private final CampaignRepositoryPort campaignRepository;

	@Override
	@Transactional(readOnly = true)
	public AbTest getAbTestById(AbTestId testId) {
		return findAbTestByIdOrThrow(testId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AbTest> searchAbTests(AbTestQuery query, Pageable pageable) {
		if (query.isEmpty()) return abTestRepository.findAll(pageable);

		return abTestRepository.findByFilters(query, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AbTest> getAbTestsByCampaign(MarketingCampaignId campaignId, Pageable pageable) {
		return abTestRepository.findByCampaignId(campaignId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AbTest> getCompletedTests(Pageable pageable) {
		return abTestRepository.findByCompletionStatus(true, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AbTest> getRunningTests(Pageable pageable) {
		return abTestRepository.findRunningTests(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AbTest> getScheduledTests(Pageable pageable) {
		return abTestRepository.findScheduledTests(pageable);
	}


	private AbTest findAbTestByIdOrThrow(AbTestId testId) {
		return abTestRepository.findById(testId)
				.orElseThrow(() -> new MarketingDomainException(
						"AB Test not found with id: " + testId.getValue()
				));
	}

}