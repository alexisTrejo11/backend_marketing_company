package at.backend.MarketingCompany.marketing.ab_test.core.application.query;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.TestType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AbTestQuery(
    MarketingCampaignId campaignId,
    List<TestType> testTypes,
    Boolean isCompleted,
    LocalDateTime startDateFrom,
    LocalDateTime startDateTo,
    BigDecimal minConfidenceLevel,
    String searchTerm
) {
  public static AbTestQuery empty() {
    return new AbTestQuery(null, null, null, null, null, null, null);
  }

  public boolean isEmpty() {
    return campaignId == null &&
           (testTypes == null || testTypes.isEmpty()) &&
           isCompleted == null &&
           startDateFrom == null &&
           startDateTo == null &&
           minConfidenceLevel == null &&
           (searchTerm == null || searchTerm.isBlank());
  }
}