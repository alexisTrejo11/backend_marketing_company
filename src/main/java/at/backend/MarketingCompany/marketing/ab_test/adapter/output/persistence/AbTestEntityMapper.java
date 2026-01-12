package at.backend.MarketingCompany.marketing.ab_test.adapter.output.persistence;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTestReconstructParams;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.AbTestEntity;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AbTestEntityMapper {
	private final ObjectMapper objectMapper;

	@Autowired
	public AbTestEntityMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}


	public AbTestEntity toEntity(AbTest domain) {
		if (domain == null) {
			return null;
		}

		AbTestEntity entity = new AbTestEntity();
		entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
		entity.setTestName(domain.getTestName());
		entity.setCampaign(
				domain.getCampaignId() != null ?
						new MarketingCampaignEntity(domain.getCampaignId().getValue()) : null
		);
		entity.setHypothesis(domain.getHypothesis());
		entity.setTestType(domain.getTestType());
		entity.setPrimaryMetric(domain.getPrimaryMetric());
		entity.setConfidenceLevel(domain.getConfidenceLevel());
		entity.setRequiredSampleSize(domain.getRequiredSampleSize());
		entity.setControlVariant(domain.getControlVariant());
		entity.setWinningVariant(domain.getWinningVariant());
		entity.setStatisticalSignificance(domain.getStatisticalSignificance());
		entity.setIsCompleted(domain.isCompleted());
		entity.setStartDate(domain.getStartDate());
		entity.setEndDate(domain.getEndDate());
		entity.setCreatedAt(domain.getCreatedAt());
		entity.setUpdatedAt(domain.getUpdatedAt());
		entity.setDeletedAt(domain.getDeletedAt());
		entity.setVersion(domain.getVersion());

		try {
			String jsonString = domain.getTreatmentVariantsJson();
			entity.setTreatmentVariants(jsonString);
			log.debug("Setting treatment variants JSON: {}", jsonString);
		} catch (Exception e) {
			log.error("Error serializing treatment variants", e);
			entity.setTreatmentVariants("{}");
		}


		return entity;
	}

	public AbTest toDomain(AbTestEntity entity) {
		if (entity == null) {
			return null;
		}

		JsonNode treatmentVariantsNode;
		try {
			if (entity.getTreatmentVariants() == null) {
				treatmentVariantsNode = objectMapper.createObjectNode();
			} else {
				treatmentVariantsNode = objectMapper.readTree(entity.getTreatmentVariants().toString());
			}
		} catch (Exception e) {
			log.error("Error parsing treatment variants JSON: {}", entity.getTreatmentVariants(), e);
			treatmentVariantsNode = objectMapper.createObjectNode();
		}

		AbTestReconstructParams params = AbTestReconstructParams.builder()
				.id(entity.getId() != null ? new AbTestId(entity.getId()) : null)
				.campaignId(entity.getCampaign() != null ? entity.getCampaign().getId() != null ? new MarketingCampaignId(entity.getCampaign().getId()) : null : null)
				.testName(entity.getTestName())
				.hypothesis(entity.getHypothesis())
				.testType(entity.getTestType())
				.primaryMetric(entity.getPrimaryMetric())
				.confidenceLevel(entity.getConfidenceLevel())
				.requiredSampleSize(entity.getRequiredSampleSize())
				.controlVariant(entity.getControlVariant())
				.treatmentVariantsJson(treatmentVariantsNode)
				.winningVariant(entity.getWinningVariant())
				.statisticalSignificance(entity.getStatisticalSignificance())
				.isCompleted(entity.getIsCompleted())
				.startDate(entity.getStartDate())
				.endDate(entity.getEndDate())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.version(entity.getVersion())
				.build();

		return AbTest.reconstruct(params);
	}

	public List<AbTest> toDomainList(List<AbTestEntity> entities) {
		if (entities == null) {
			return List.of();
		}
		return entities.stream()
				.map(this::toDomain)
				.toList();
	}
}
