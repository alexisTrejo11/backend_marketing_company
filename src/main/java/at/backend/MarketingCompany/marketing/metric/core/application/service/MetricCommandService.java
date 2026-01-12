package at.backend.MarketingCompany.marketing.metric.core.application.service;

import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.marketing.metric.core.application.command.CreateMetricCommand;
import at.backend.MarketingCompany.marketing.metric.core.application.command.UpdateMetricCommand;
import at.backend.MarketingCompany.marketing.metric.core.application.command.UpdateMetricValueCommand;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CreateMetricParams;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.MetricValidator;
import at.backend.MarketingCompany.marketing.metric.core.domain.exception.MetricNotFoundException;
import at.backend.MarketingCompany.marketing.metric.core.domain.exception.MetricValidationException;
import at.backend.MarketingCompany.marketing.metric.core.domain.valueobject.CampaignMetricId;
import at.backend.MarketingCompany.marketing.metric.core.port.input.MetricCommandInputPort;
import at.backend.MarketingCompany.marketing.metric.core.port.output.MetricOutputPort;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricCommandService implements MetricCommandInputPort {

	private final MetricOutputPort metricRepository;
	private final CampaignRepositoryPort campaignRepository;

	@Override
	@Transactional
	public CampaignMetric createMetric(CreateMetricCommand command) {
		log.debug("Creating metric for campaign: {}, name: {}", command.campaignId().getValue(), command.name());

		if (!campaignRepository.existsById(command.campaignId())) {
			throw new MetricValidationException(command.campaignId());
		}
		if (metricRepository.existsByCampaignIdAndName(command.campaignId(), command.name())) {
			throw new MetricValidationException("Metric name already exists for this campaign: " + command.name());
		}

		CreateMetricParams params = command.toCreateParams();
		CampaignMetric metric = CampaignMetric.create(params);

		CampaignMetric savedMetric = metricRepository.save(metric);
		log.info("Metric created successfully with ID: {}", savedMetric.getId().getValue());

		return savedMetric;
	}

	@Override
	@Transactional
	public CampaignMetric updateMetric(UpdateMetricCommand command) {
		log.debug("Updating metric: {}", command.metricId().getValue());

		CampaignMetric metric = findMetricByIdOrThrow(command.metricId());
		metric.updateGeneralInfo(
				command.description(),
				command.targetValue(),
				command.calculationFormula(),
				command.dataSource()
		);

		CampaignMetric updatedMetric = metricRepository.save(metric);
		log.info("Metric updated successfully: {}", command.metricId().getValue());

		return updatedMetric;
	}

	@Override
	@Transactional
	public CampaignMetric updateMetricValue(UpdateMetricValueCommand command) {
		log.debug("Updating metric value: {} to {}", command.metricId().getValue(), command.newValue());

		CampaignMetric metric = findMetricByIdOrThrow(command.metricId());
		metric.updateValue(command.newValue());

		CampaignMetric updatedMetric = metricRepository.save(metric);
		log.info("Metric value updated successfully: {}", command.metricId().getValue());

		return updatedMetric;
	}

	@Override
	@Transactional
	public CampaignMetric markAsAutomated(CampaignMetricId metricId) {
		log.debug("Marking metric as automated: {}", metricId.getValue());

		CampaignMetric metric = findMetricByIdOrThrow(metricId);
		metric.markAsAutomated();

		CampaignMetric updatedMetric = metricRepository.save(metric);
		log.info("Metric marked as automated: {}", metricId.getValue());

		return updatedMetric;
	}

	@Override
	@Transactional
	public void deleteMetric(CampaignMetricId metricId) {
		log.debug("Deleting metric: {}", metricId.getValue());

		CampaignMetric metric = findMetricByIdOrThrow(metricId);
		metric.softDelete();

		metricRepository.save(metric);

		log.info("Metric deleted successfully: {}", metricId.getValue());
	}

	private CampaignMetric findMetricByIdOrThrow(CampaignMetricId metricId) {
		return metricRepository.findById(metricId)
				.orElseThrow(() -> new MetricNotFoundException(metricId));
	}
}