package at.backend.MarketingCompany.marketing.activity.core.port.input;

import at.backend.MarketingCompany.marketing.activity.core.application.command.CreateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.RecordActivityDatesCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCommand;
import at.backend.MarketingCompany.marketing.activity.core.application.command.UpdateActivityCostCommand;
import at.backend.MarketingCompany.marketing.activity.core.domain.entity.CampaignActivity;
import at.backend.MarketingCompany.marketing.activity.core.domain.valueobject.CampaignActivityId;

public interface CampaignActivityCommandServicePort {

  /**
   * Creates a new campaign activity
   */
  CampaignActivity createActivity(CreateActivityCommand command);
  
  /**
   * Updates an activity
   */
  CampaignActivity updateActivity(UpdateActivityCommand command);
  
  /**
   * Starts an activity (transitions to IN_PROGRESS)
   */
  CampaignActivity startActivity(CampaignActivityId activityId);
  
  /**
   * Completes an activity
   */
  CampaignActivity completeActivity(CampaignActivityId activityId, String completionNotes);
  
  /**
   * Cancels an activity
   */
  CampaignActivity cancelActivity(CampaignActivityId activityId, String cancelReason);
  
  /**
   * Blocks an activity
   */
  CampaignActivity blockActivity(CampaignActivityId activityId, String blockReason);
  
  /**
   * Updates activity actual cost
   */
  CampaignActivity updateActivityCost(UpdateActivityCostCommand command);
  
  /**
   * Records actual dates for an activity
   */
  CampaignActivity recordActivityDates(RecordActivityDatesCommand command);
  
  /**
   * Assigns activity to a user
   */
  CampaignActivity assignToUser(CampaignActivityId activityId, Long userId);

  CampaignActivity unassign(CampaignActivityId activityId);

  /**
   * Deletes an activity (soft delete)
   */
  void deleteActivity(CampaignActivityId activityId);
  

  

}
