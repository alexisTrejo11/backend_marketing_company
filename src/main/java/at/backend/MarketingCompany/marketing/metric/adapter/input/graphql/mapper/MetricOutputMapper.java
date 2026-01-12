package at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.marketing.metric.adapter.input.graphql.dto.CampaignMetricOutput;
import at.backend.MarketingCompany.marketing.metric.core.domain.entity.CampaignMetric;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MetricOutputMapper {

	public CampaignMetricOutput toOutput(CampaignMetric metric) {
		BigDecimal achievementPercentage = BigDecimal.ZERO;
		if (metric.getTargetValue() != null && metric.getTargetValue().compareTo(BigDecimal.ZERO) > 0) {
			achievementPercentage = metric.getCurrentValue()
					.multiply(BigDecimal.valueOf(100))
					.divide(metric.getTargetValue(), 2, BigDecimal.ROUND_HALF_UP);
		}

		return new CampaignMetricOutput(
				metric.getId().getValue(),
				metric.getCampaignId().getValue(),
				metric.getName(),
				metric.getMetricType().name(),
				metric.getDescription(),
				metric.getCurrentValue(),
				metric.getTargetValue(),
				metric.getMeasurementUnit(),
				metric.getCalculationFormula(),
				metric.getDataSource(),
				metric.getLastCalculatedDate() != null ? metric.getLastCalculatedDate().toString() : null,
				metric.isAutomated(),
				metric.isTargetAchieved(),
				achievementPercentage,
				metric.getCreatedAt() != null ? metric.getCreatedAt().toString() : null,
				metric.getUpdatedAt() != null ? metric.getUpdatedAt().toString() : null
		);
	}

	public PageResponse<CampaignMetricOutput> toPageResponse(Page<CampaignMetric> metricPage) {
		if (metricPage == null) {
			return null;
		}

		return PageResponse.of(metricPage.map(this::toOutput));
	}
}
