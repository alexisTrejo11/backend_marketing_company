package at.backend.MarketingCompany.marketing.ab_test.core.domain;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;
import com.fasterxml.jackson.databind.JsonNode;
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
		JsonNode treatmentVariants,
		LocalDateTime startDate,
		LocalDateTime endDate,
		Integer requiredSampleSize,
		BigDecimal confidenceLevel,
		String hypothesis,
		String winningVariant,
		BigDecimal statisticalSignificance
) {
}
