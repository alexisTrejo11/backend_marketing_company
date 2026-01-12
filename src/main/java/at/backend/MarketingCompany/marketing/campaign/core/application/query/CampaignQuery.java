package at.backend.MarketingCompany.marketing.campaign.core.application.query;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CampaignQuery(
		CampaignStatus status,
		LocalDate startDateFrom,
		LocalDate startDateTo,
		LocalDate endDateFrom,
		LocalDate endDateTo,
		BigDecimal minBudget,
		BigDecimal maxBudget,
		Boolean isActive,
		String searchTerm
) {
	public static CampaignQuery empty() {
		return new CampaignQuery(null, null, null, null, null, null, null, null, null);
	}

	public boolean isEmpty() {
		return status == null && startDateFrom == null && startDateTo == null &&
				endDateFrom == null && endDateTo == null && minBudget == null &&
				maxBudget == null && isActive == null &&
				(searchTerm == null || searchTerm.isBlank());
	}
}