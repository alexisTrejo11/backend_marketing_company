package at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.repository;


import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.mapper.MarketingInteractionEntityMapper;
import at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.model.CampaignInteractionEntity;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import at.backend.MarketingCompany.marketing.interaction.core.port.output.InteractionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InteractionRepositoryAdapter implements InteractionRepositoryPort {
	private final CampaignInteractionJpaRepository jpaRepository;
	private final MarketingInteractionEntityMapper mapper;

	@Override
	public CampaignInteraction save(CampaignInteraction interaction) {
		CampaignInteractionEntity entity = mapper.toEntity(interaction);
		CampaignInteractionEntity savedEntity = jpaRepository.save(entity);
		return mapper.toDomain(savedEntity);
	}

	@Override
	public Optional<CampaignInteraction> findById(CampaignInteractionId id) {
		return jpaRepository.findByIdAndNotDeleted(id.getValue())
				.map(mapper::toDomain);
	}

	@Override
	public void delete(CampaignInteractionId id) {
		jpaRepository.deleteById(id.getValue());
	}

	@Override
	public Page<CampaignInteraction> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignInteraction> findByCustomerId(CustomerCompanyId customerId, Pageable pageable) {
		return jpaRepository.findByCustomerId(customerId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignInteraction> findByCampaignIdAndConversionStatus(
			MarketingCampaignId campaignId,
			Boolean isConversion,
			Pageable pageable) {
		return jpaRepository.findByCampaignIdAndConversionStatus(campaignId.getValue(), isConversion, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignInteraction> findByDateRange(
			LocalDateTime startDate,
			LocalDateTime endDate,
			Pageable pageable) {
		return jpaRepository.findByDateRange(startDate, endDate, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignInteraction> findByCampaignIdAndInteractionType(
			MarketingCampaignId campaignId,
			MarketingInteractionType marketingInteractionType,
			Pageable pageable) {
		return jpaRepository.findByCampaignIdAndInteractionType(campaignId.getValue(), marketingInteractionType, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignInteraction> findByChannelId(MarketingChannelId channelId, Pageable pageable) {
		return jpaRepository.findByChannelId(channelId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	public long countConversionsByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countConversionsByCampaignId(campaignId.getValue());
	}

	@Override
	public BigDecimal calculateTotalConversionValueByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.calculateTotalConversionValueByCampaignId(campaignId.getValue());
	}

	@Override
	public long countUniqueCustomersByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countUniqueCustomersByCampaignId(campaignId.getValue());
	}
}