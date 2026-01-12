package at.backend.MarketingCompany.marketing.metric.core.domain.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.metric.core.domain.exception.MetricValidationException;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class MetricValidator {

	public static void validateForCreation(CreateMetricParams params) {
		if (params == null) {
			log.warn("validateForCreation called with null params");
			throw new MetricValidationException("Creation parameters are required");
		}

		var campaignId = params.campaignId();
		var name = params.name();
		var metricType = params.metricType();
		var targetValue = params.targetValue();

		if (campaignId == null) {
			log.debug("Validation failed: missing campaignId");
			throw new MetricValidationException("Campaign ID is required");
		}

		if (name == null || name.isBlank()) {
			log.debug("Validation failed: invalid metric name");
			throw new MetricValidationException("Metric name is required");
		}

		if (name.length() > 100) {
			log.debug("Validation failed: metric name too long: {}", name);
			throw new MetricValidationException("Metric name cannot exceed 100 characters");
		}

		if (metricType == null) {
			log.debug("Validation failed: missing metric type");
			throw new MetricValidationException("Metric type is required");
		}

		if (targetValue != null && targetValue.compareTo(BigDecimal.ZERO) < 0) {
			log.debug("Validation failed: negative target value: {}", targetValue);
			throw new MetricValidationException("Target value cannot be negative");
		}

		if (metricType == MetricType.PERCENTAGE) {
			if (targetValue != null && (targetValue.compareTo(BigDecimal.ZERO) < 0 || targetValue.compareTo(new BigDecimal("100")) > 0)) {
				log.debug("Validation failed: percentage target out of range: {}", targetValue);
				throw new MetricValidationException("Target value for percentage metrics must be between 0 and 100");
			}
		}

		if (metricType == MetricType.CURRENCY || metricType == MetricType.COUNT) {
			if (targetValue != null && targetValue.compareTo(BigDecimal.ZERO) < 0) {
				log.debug("Validation failed: negative target for type {}: {}", metricType, targetValue);
				throw new MetricValidationException("Target value for " + metricType + " metrics cannot be negative");
			}
		}
	}

	public static void validateValueUpdate(BigDecimal newValue) {
		if (newValue == null) {
			log.debug("Validation failed: new metric value is null");
			throw new MetricValidationException("Metric value is required");
		}
		if (newValue.compareTo(BigDecimal.ZERO) < 0) {
			log.debug("Validation failed: negative metric value: {}", newValue);
			throw new MetricValidationException("Metric value cannot be negative");
		}
	}

	public static void validateForUpdate(String description, BigDecimal targetValue, String calculationFormula, String dataSource) {
		if (description != null && description.isBlank()) {
			log.debug("Validation failed: description provided but blank");
			throw new MetricValidationException("Metric description cannot be blank, if provided");
		}

		if (targetValue != null && targetValue.compareTo(BigDecimal.ZERO) < 0) {
			log.debug("Validation failed: negative target value on update: {}", targetValue);
			throw new MetricValidationException("Target value cannot be negative, if provided");
		}

		if (calculationFormula != null && calculationFormula.isBlank()) {
			log.debug("Validation failed: calculation formula provided but blank");
			throw new MetricValidationException("Calculation formula cannot be blank, if provided");
		}

		if (dataSource != null && dataSource.isBlank()) {
			log.debug("Validation failed: data source provided but blank");
			throw new MetricValidationException("Data source cannot be blank, if provided");
		}
	}
}
