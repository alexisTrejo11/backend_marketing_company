package at.backend.MarketingCompany.marketing.ab_test.core.domain;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record AbTestCreateParams(
		MarketingCampaignId campaignId,
		String testName,
		TestType testType,
		String primaryMetric,
		String controlVariant,
		Map<String, Object> treatmentVariants,
		LocalDateTime startDate,
		Integer requiredSampleSize,
		BigDecimal confidenceLevel
) {
}
