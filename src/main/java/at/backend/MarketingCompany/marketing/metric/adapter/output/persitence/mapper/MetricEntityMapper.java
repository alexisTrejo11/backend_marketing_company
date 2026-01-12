package at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.mapper;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.model.CampaignMetricEntity;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetricReconstructParams;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import org.springframework.stereotype.Component;

@Component
public class MetricEntityMapper {

	public CampaignMetric toDomain(CampaignMetricEntity entity) {
		if (entity == null) {
			return null;
		}

		var params = CampaignMetricReconstructParams.builder()
				.id(entity.getId() != null ? new CampaignMetricId(entity.getId()) : null)
				.campaignId(entity.getCampaign() != null ? new MarketingCampaignId(entity.getCampaign().getId()) : null)
				.metricType(entity.getMetricType())
				.name(entity.getName())
				.description(entity.getDescription())
				.currentValue(entity.getCurrentValue())
				.targetValue(entity.getTargetValue())
				.measurementUnit(entity.getMeasurementUnit())
				.calculationFormula(entity.getCalculationFormula())
				.dataSource(entity.getDataSource())
				.lastCalculatedDate(entity.getLastCalculatedDate())
				.isAutomated(entity.getIsAutomated())
				.isTargetAchieved(entity.getIsTargetAchieved())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.deletedAt(entity.getDeletedAt())
				.version(entity.getVersion())
				.build();

		return CampaignMetric.reconstruct(params);
	}

	public CampaignMetricEntity toEntity(CampaignMetric metric) {
		if (metric == null) {
			return null;
		}

		CampaignMetricEntity entity = new CampaignMetricEntity();
		entity.setId(metric.getId() != null ? metric.getId().getValue() : null);
		entity.setCampaign(metric.getCampaignId() != null ? new MarketingCampaignEntity(metric.getCampaignId().getValue()) : null);
		entity.setName(metric.getName());
		entity.setMetricType(metric.getMetricType());
		entity.setDescription(metric.getDescription());
		entity.setCurrentValue(metric.getCurrentValue());
		entity.setTargetValue(metric.getTargetValue());
		entity.setMeasurementUnit(metric.getMeasurementUnit());
		entity.setCalculationFormula(metric.getCalculationFormula());
		entity.setDataSource(metric.getDataSource());
		entity.setLastCalculatedDate(metric.getLastCalculatedDate());
		entity.setIsAutomated(metric.isAutomated());
		entity.setIsTargetAchieved(metric.isTargetAchieved());
		entity.setCreatedAt(metric.getCreatedAt());
		entity.setUpdatedAt(metric.getUpdatedAt());
		entity.setDeletedAt(metric.getDeletedAt());
		entity.setVersion(metric.getVersion());
		return entity;
	}
}
