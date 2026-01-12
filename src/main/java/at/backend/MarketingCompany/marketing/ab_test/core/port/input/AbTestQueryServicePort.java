package at.backend.MarketingCompany.marketing.ab_test.core.port.input;

import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface AbTestQueryServicePort {
  AbTest getAbTestById(AbTestId testId);
  Page<AbTest> searchAbTests(AbTestQuery query, Pageable pageable);
  Page<AbTest> getAbTestsByCampaign(MarketingCampaignId campaignId, Pageable pageable);
  Page<AbTest> getCompletedTests(Pageable pageable);
  Page<AbTest> getRunningTests(Pageable pageable);
  Page<AbTest> getScheduledTests(Pageable pageable);
}