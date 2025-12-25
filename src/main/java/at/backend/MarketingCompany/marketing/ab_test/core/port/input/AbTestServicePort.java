package at.backend.MarketingCompany.marketing.ab_test.core.port.input;

import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CompleteAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.CreateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.command.UpdateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.application.query.AbTestQuery;
import at.backend.MarketingCompany.marketing.ab_test.core.application.statistics.AbTestStatistics;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AbTestServicePort {

  AbTest createAbTest(CreateAbTestCommand command);
  AbTest updateAbTest(UpdateAbTestCommand command);
  AbTest completeAbTest(CompleteAbTestCommand command);
  void deleteAbTest(AbTestId testId);

  
  AbTest getAbTestById(AbTestId testId);
  Page<AbTest> searchAbTests(AbTestQuery query, Pageable pageable);
  Page<AbTest> getAbTestsByCampaign(MarketingCampaignId campaignId, Pageable pageable);
  Page<AbTest> getCompletedTests(Pageable pageable);
  Page<AbTest> getRunningTests(Pageable pageable);
  Page<AbTest> getScheduledTests(Pageable pageable);
  

  AbTestStatistics getAbTestStatistics(MarketingCampaignId campaignId);
  Double getCompletionRate(MarketingCampaignId campaignId);
  BigDecimal getAverageSignificance(MarketingCampaignId campaignId);
}