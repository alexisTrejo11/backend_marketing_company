package at.backend.MarketingCompany.marketing.ab_test.core.application.service;

import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CompleteAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CreateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.UpdateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTestCreateParams;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTestValidator;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.exception.AbTestNotFoundException;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.ab_test.core.port.input.AbTestCommandServicePort;

import at.backend.MarketingCompany.marketing.ab_test.core.port.output.AbTestRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingCampaignNotFoundException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbTestCommandService implements AbTestCommandServicePort {
	private final AbTestRepositoryPort abTestRepository;
	private final CampaignRepositoryPort campaignRepository;

	@Override
	@Transactional
	public AbTest createAbTest(CreateAbTestCommand command) {
		log.info("Creating AB test for campaign: {}, name: {}", command.campaignId().getValue(), command.testName());

		if (!campaignRepository.existsById(command.campaignId())) {
			throw new MarketingCampaignNotFoundException(command.campaignId());
		}

		if (abTestRepository.existsByNameAndCampaignId(command.testName(), command.campaignId())) {
			throw new BusinessRuleException("AB Test name already exists for this campaign: " + command.testName()
			);
		}

		log.info("AB test creation parameters validated successfully for test name: {}", command.testName());

		AbTestCreateParams createParams = command.toAbTestCreateParams();
		AbTest test = AbTest.create(createParams);

		log.info("AB test instance created successfully for test name: {}", command.testName());

		AbTest savedTest = abTestRepository.save(test);
		log.info("AB test created successfully with ID: {}", savedTest.getId().getValue());
		return savedTest;
	}

	@Override
	@Transactional
	public AbTest updateAbTest(UpdateAbTestCommand command) {
		log.info("Updating AB test: {}", command.testId());

		AbTest test = findAbTestByIdOrThrow(command.testId());

		var params = command.toUpdateParams(test.getStartDate());
		test.update(params);

		AbTest updatedTest = abTestRepository.save(test);
		log.info("AB test updated successfully: {}", command.testId().getValue());

		return updatedTest;
	}

	@Override
	@Transactional
	public AbTest completeAbTest(CompleteAbTestCommand command) {
		log.info("Completing AB test: {}", command.testId().getValue());

		AbTest test = findAbTestByIdOrThrow(command.testId());
		test.complete(command.winningVariant(), command.statisticalSignificance());

		AbTest completedTest = abTestRepository.save(test);
		log.info("AB test completed successfully: {}", command.testId().getValue());

		return completedTest;
	}

	@Override
	@Transactional
	public void deleteAbTest(AbTestId testId) {
		log.info("Deleting AB test: {}", testId.getValue());

		AbTest test = findAbTestByIdOrThrow(testId);
		test.softDelete();

		abTestRepository.save(test);
		log.info("AB test soft-deleted successfully: {}", testId.getValue());
	}

	private AbTest findAbTestByIdOrThrow(AbTestId testId) {
		return abTestRepository.findById(testId).orElseThrow(() -> new AbTestNotFoundException(testId));
	}
}