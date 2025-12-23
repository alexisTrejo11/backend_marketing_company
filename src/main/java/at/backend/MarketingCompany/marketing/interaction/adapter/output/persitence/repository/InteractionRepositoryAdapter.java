package at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.repository;


import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.mapper.MarketingInteractionEntityMapper;
import at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.model.CampaignInteractionEntity;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.port.output.InteractionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InteractionRepositoryAdapter implements InteractionRepositoryPort {
	private final CampaignInteractionJpaRepository jpaRepository;
	private final MarketingInteractionEntityMapper mapper;

	@Override
	public CampaignInteraction save(CampaignInteraction interaction) {
		CampaignInteractionEntity entity = mapper.toEntity(interaction);
		entity.processNewEntityIfNeeded();

		CampaignInteractionEntity savedEntity = jpaRepository.saveAndFlush(entity);
		return mapper.toDomain(savedEntity);
	}

	@Override
	public Optional<CampaignInteraction> findById(CampaignInteractionId id) {
		return jpaRepository.findByIdAndNotDeleted(id.getValue())
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignInteraction> findAll(Pageable pageable) {
		return jpaRepository.findAllNotDeleted(pageable)
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
	public Map<String, Long> countByInteractionTypeByCampaignId(MarketingCampaignId campaignId) {
		List<Object[]> results = jpaRepository.countByInteractionTypeByCampaignId(campaignId.getValue());
		Map<String, Long> map = new java.util.HashMap<>();
		for (Object[] result : results) {
			String type = result[0] != null ? result[0].toString() : "UNKNOWN";
			Long count = result[1] != null ? ((Number) result[1]).longValue() : 0L;
			map.put(type, count);
		}
		return map;
	}

	@Override
	public Map<String, Long> countByDeviceTypeByCampaignId(MarketingCampaignId campaignId) {
		List<Object[]> results = jpaRepository.countByDeviceTypeByCampaignId(campaignId.getValue());
		Map<String, Long> map = new java.util.HashMap<>();
		for (Object[] result : results) {
			String deviceType = result[0] != null ? result[0].toString() : "UNKNOWN";
			Long count = result[1] != null ? ((Number) result[1]).longValue() : 0L;
			map.put(deviceType, count);
		}
		return map;
	}

	@Override
	public Page<CampaignInteraction> findByCustomerId(CustomerCompanyId customerId, Pageable pageable) {
		return jpaRepository.findByCustomerId(customerId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignInteraction> findByFilters(Pageable pageable) {
		return jpaRepository.findAllNotDeleted(pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignInteraction> findByCampaignIdAndIsConversion(
			MarketingCampaignId campaignId,
			boolean isConversion,
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
	public Long countByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countByCampaignId(campaignId.getValue());
	}

	@Override
	public Page<CampaignInteraction> findByChannelId(MarketingChannelId channelId, Pageable pageable) {
		return jpaRepository.findByChannelId(channelId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Long countConversionsByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countConversionsByCampaignId(campaignId.getValue());
	}

	@Override
	public Page<CampaignInteraction> findByUtmParameters(String utmSource, String utmMedium, String utmCampaign, Pageable pageable) {
		return jpaRepository.findByUtmParameters(utmSource, utmMedium, utmCampaign, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public BigDecimal calculateTotalConversionValueByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.calculateTotalConversionValueByCampaignId(campaignId.getValue());
	}

	@Override
	public Long countUniqueCustomersByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countUniqueCustomersByCampaignId(campaignId.getValue());
	}

	@Override
	public Long countUniqueChannelsByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countUniqueChannelsByCampaignId(campaignId.getValue());
	}

	@Override
	public Map<String, Long> findTopCountriesByCampaignId(MarketingCampaignId campaignId, int limit) {
		Pageable pageable = PageRequest.of(0, limit);
		List<Object[]> results = jpaRepository.findTopCountriesByCampaignId(campaignId.getValue(), pageable);
		Map<String, Long> map = new java.util.LinkedHashMap<>();
		for (Object[] result : results) {
			String country = result[0] != null ? result[0].toString() : "UNKNOWN";
			Long count = result[1] != null ? ((Number) result[1]).longValue() : 0L;
			map.put(country, count);
		}
		return map;
	}

	@Override
	public Map<String, Long> findTopCitiesByCampaignId(MarketingCampaignId campaignId, int limit) {
		Pageable pageable = PageRequest.of(0, limit);
		List<Object[]> results = jpaRepository.findTopCitiesByCampaignId(campaignId.getValue(), pageable);
		Map<String, Long> map = new java.util.LinkedHashMap<>();
		for (Object[] result : results) {
			String city = result[0] != null ? result[0].toString() : "UNKNOWN";
			Long count = result[1] != null ? ((Number) result[1]).longValue() : 0L;
			map.put(city, count);
		}
		return map;
	}
}