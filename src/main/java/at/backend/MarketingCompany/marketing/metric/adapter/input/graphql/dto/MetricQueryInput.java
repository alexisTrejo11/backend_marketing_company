package at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.metric.core.application.query.MetricQuery;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MetricQueryInput(
		@Positive Long campaignId,
		@Size(max = 20) List<MetricType> metricTypes,
		Boolean isTargetAchieved,
		Boolean isAutomated,
		BigDecimal minCurrentValue,
		BigDecimal maxCurrentValue,
		LocalDateTime lastCalculatedAfter,
		@Length(max = 255) String searchTerm
) {
	public MetricQuery toQuery() {
		return new MetricQuery(
				campaignId,
				metricTypes,
				isTargetAchieved,
				isAutomated,
				minCurrentValue,
				maxCurrentValue,
				lastCalculatedAfter,
				searchTerm
		);
	}
}