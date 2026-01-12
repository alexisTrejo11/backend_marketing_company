package at.backend.MarketingCompany.marketing.campaign.core.application.service;

import at.backend.MarketingCompany.marketing.campaign.core.application.command.AddCampaignSpendingCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.CreateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.UpdateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingCampaignNotFoundException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.CampaignValidator;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.input.CampaignCommandServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignCommandServiceImpl implements CampaignCommandServicePort {
	private final CampaignRepositoryPort campaignRepository;

	@Override
	@Transactional
	public MarketingCampaign createCampaign(CreateCampaignCommand command) {
		log.debug("Creating campaign with name: {}", command.name().value());

		CampaignValidator.validateForCreation(command.name(), command.totalBudget());

		if (campaignRepository.existsByNameAndNotDeleted(command.name().value())) {
			throw new BusinessRuleException("Campaign name already exists: " + command.name().value());
		}

		MarketingCampaign campaign = MarketingCampaign.create(
				command.name(),
				command.campaignType(),
				command.totalBudget(),
				command.period(),
				command.primaryGoal(),
				command.description()
		);

		if (command.targetAudienceDemographics() != null) {
			campaign.updateTargetAudience(command.targetAudienceDemographics());
		}
		if (command.targetLocations() != null) {
			campaign.updateTargetLocations(command.targetLocations());
		}
		if (command.primaryChannelId() != null) {
			campaign.assignPrimaryChannel(command.primaryChannelId());
		}

		MarketingCampaign savedCampaign = campaignRepository.save(campaign);
		log.info("Campaign created successfully with ID: {}", savedCampaign.getId().getValue());

		return savedCampaign;
	}

	@Override
	@Transactional
	public MarketingCampaign updateCampaign(UpdateCampaignCommand command) {
		log.debug("Updating campaign with ID: {}", command.campaignId().getValue());

		MarketingCampaign campaign = findCampaignByIdOrThrow(command.campaignId());

		// Validate update is allowed
		CampaignValidator.validateForUpdate(campaign);

		// Check for duplicate name (excluding current campaign)
		if (command.name() != null &&
				!campaign.getName().equals(command.name()) &&
				campaignRepository.existsByNameAndNotDeleted(command.name().value())) {
			throw new BusinessRuleException("Campaign name already exists: " + command.name().value());
		}

		// Update campaign fields
		campaign.updateGeneralInfo(
				command.name(),
				command.description(),
				command.campaignType(),
				command.period(),
				command.primaryGoal()
		);

		if (command.targetAudienceDemographics() != null) {
			campaign.updateTargetAudience(command.targetAudienceDemographics());
		}
		if (command.targetLocations() != null) {
			campaign.updateTargetLocations(command.targetLocations());
		}
		if (command.primaryChannelId() != null) {
			campaign.assignPrimaryChannel(command.primaryChannelId());
		}

		MarketingCampaign updatedCampaign = campaignRepository.save(campaign);
		log.info("Campaign updated successfully: {}", campaign.getId().getValue());

		return updatedCampaign;
	}

	@Override
	@Transactional
	public void deleteCampaign(MarketingCampaignId campaignId) {
		log.debug("Deleting campaign with ID: {}", campaignId.getValue());

		MarketingCampaign campaign = findCampaignByIdOrThrow(campaignId);

		// Validate deletion is allowed
		CampaignValidator.validateForDeletion(campaign);

		campaign.softDelete();
		campaignRepository.save(campaign);

		log.info("Campaign deleted successfully: {}", campaignId.getValue());
	}

	@Override
	@Transactional
	public MarketingCampaign launchCampaign(MarketingCampaignId campaignId) {
		log.debug("Launching campaign with ID: {}", campaignId.getValue());

		MarketingCampaign campaign = findCampaignByIdOrThrow(campaignId);
		campaign.launch();

		MarketingCampaign launchedCampaign = campaignRepository.save(campaign);
		log.info("Campaign launched successfully: {}", campaignId.getValue());

		return launchedCampaign;
	}

	@Override
	@Transactional
	public MarketingCampaign pauseCampaign(MarketingCampaignId campaignId) {
		log.debug("Pausing campaign with ID: {}", campaignId.getValue());

		MarketingCampaign campaign = findCampaignByIdOrThrow(campaignId);
		campaign.pause();

		MarketingCampaign pausedCampaign = campaignRepository.save(campaign);
		log.info("Campaign paused successfully: {}", campaignId.getValue());

		return pausedCampaign;
	}

	@Override
	@Transactional
	public MarketingCampaign resumeCampaign(MarketingCampaignId campaignId) {
		log.debug("Resuming campaign with ID: {}", campaignId.getValue());

		MarketingCampaign campaign = findCampaignByIdOrThrow(campaignId);
		campaign.resume();

		MarketingCampaign resumedCampaign = campaignRepository.save(campaign);
		log.info("Campaign resumed successfully: {}", campaignId.getValue());

		return resumedCampaign;
	}

	@Override
	@Transactional
	public MarketingCampaign completeCampaign(MarketingCampaignId campaignId) {
		log.debug("Completing campaign with ID: {}", campaignId.getValue());

		MarketingCampaign campaign = findCampaignByIdOrThrow(campaignId);
		campaign.complete();

		MarketingCampaign completedCampaign = campaignRepository.save(campaign);
		log.info("Campaign completed successfully: {}", campaignId.getValue());

		return completedCampaign;
	}

	@Override
	@Transactional
	public MarketingCampaign cancelCampaign(MarketingCampaignId campaignId) {
		log.debug("Cancelling campaign with ID: {}", campaignId.getValue());

		MarketingCampaign campaign = findCampaignByIdOrThrow(campaignId);
		campaign.cancel();

		MarketingCampaign cancelledCampaign = campaignRepository.save(campaign);
		log.info("Campaign cancelled successfully: {}", campaignId.getValue());

		return cancelledCampaign;
	}

	@Override
	@Transactional
	public MarketingCampaign addSpending(AddCampaignSpendingCommand command) {
		log.debug("Adding spending of {} to campaign: {}",
				command.amount(), command.campaignId().getValue());

		MarketingCampaign campaign = findCampaignByIdOrThrow(command.campaignId());

		// Validate spending amount
		CampaignValidator.validateSpendingAmount(command.amount(), campaign);

		campaign.addSpending(command.amount());

		MarketingCampaign updatedCampaign = campaignRepository.save(campaign);
		log.info("Spending added successfully to campaign: {}", command.campaignId().getValue());

		return updatedCampaign;
	}

	@Override
	@Transactional
	public MarketingCampaign updateAttributedRevenue(
			MarketingCampaignId campaignId,
			BigDecimal revenue) {

		log.debug("Updating attributed revenue for campaign: {}", campaignId.getValue());

		MarketingCampaign campaign = findCampaignByIdOrThrow(campaignId);
		campaign.updateAttributedRevenue(revenue);

		MarketingCampaign updatedCampaign = campaignRepository.save(campaign);
		log.info("Attributed revenue updated for campaign: {}", campaignId.getValue());

		return updatedCampaign;
	}

	private MarketingCampaign findCampaignByIdOrThrow(MarketingCampaignId campaignId) {
		return campaignRepository.findById(campaignId)
				.orElseThrow(() -> new MarketingCampaignNotFoundException(campaignId));
	}
}