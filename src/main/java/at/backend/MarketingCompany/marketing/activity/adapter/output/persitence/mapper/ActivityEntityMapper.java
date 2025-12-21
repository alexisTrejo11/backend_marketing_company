package at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.mapper;

import at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.model.CampaignActivityEntity;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivityReconstructParams;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivityCost;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.ActivitySchedule;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.stereotype.Component;

@Component
public class ActivityEntityMapper {


	public CampaignActivityEntity toEntity(CampaignActivity domain) {
		if (domain == null) {
			return null;
		}

		CampaignActivityEntity entity = new CampaignActivityEntity();
		entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
		entity.setCampaign(domain.getCampaignId() != null ? new MarketingCampaignEntity(domain.getCampaignId().getValue()) : null);
		entity.setName(domain.getName());
		entity.setDescription(domain.getDescription());
		entity.setActivityType(domain.getActivityType());
		entity.setStatus(domain.getStatus());
		entity.setPlannedStartDate(domain.getSchedule() != null ? domain.getSchedule().plannedStartDate() : null);
		entity.setPlannedEndDate(domain.getSchedule() != null ? domain.getSchedule().plannedEndDate() : null);
		entity.setActualStartDate(domain.getSchedule() != null ? domain.getSchedule().actualStartDate() : null);
		entity.setActualEndDate(domain.getSchedule() != null ? domain.getSchedule().actualEndDate() : null);
		entity.setPlannedCost(domain.getCost() != null ? domain.getCost().plannedCost() : null);
		entity.setActualCost(domain.getCost() != null ? domain.getCost().actualCost() : null);
		entity.setAssignedToUserId(domain.getAssignedToUserId());
		entity.setDeliveryChannel(domain.getDeliveryChannel());
		entity.setSuccessCriteria(domain.getSuccessCriteria());
		entity.setTargetAudience(domain.getTargetAudience());
		entity.setDependencies(domain.getDependencies());
		entity.setCreatedAt(domain.getCreatedAt());
		entity.setUpdatedAt(domain.getUpdatedAt());
		entity.setDeletedAt(domain.getDeletedAt());
		entity.setVersion(domain.getVersion());

		return entity;
	}

	public CampaignActivity toDomain(CampaignActivityEntity entity) {
		if (entity == null) {
			return null;

		}

		var cost = new ActivityCost(
				entity.getPlannedCost(),
				entity.getActualCost()
		);

		var schedule = new ActivitySchedule(
				entity.getPlannedStartDate(),
				entity.getPlannedEndDate(),
				entity.getActualStartDate(),
				entity.getActualEndDate()
		);

		var params = CampaignActivityReconstructParams.builder()
				.id(entity.getId() != null ? new CampaignActivityId(entity.getId()) : null)
				.campaignId(entity.getCampaign() != null ? new MarketingCampaignId(entity.getCampaign().getId()) : null)
				.name(entity.getName())
				.description(entity.getDescription())
				.activityType(entity.getActivityType())
				.status(entity.getStatus())
				.schedule(schedule) // TODO: Map schedule fields
				.cost(cost)
				.assignedToUserId(entity.getAssignedToUserId())
				.deliveryChannel(entity.getDeliveryChannel())
				.successCriteria(entity.getSuccessCriteria())
				.targetAudience(entity.getTargetAudience())
				.dependencies(entity.getDependencies())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.deletedAt(entity.getDeletedAt())
				.version(entity.getVersion())
				.build();

		return CampaignActivity.reconstruct(params);
	}
}
