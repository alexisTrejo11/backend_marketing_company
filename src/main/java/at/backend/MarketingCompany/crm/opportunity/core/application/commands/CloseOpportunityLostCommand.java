package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.LossReason;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;

public record CloseOpportunityLostCommand(
    OpportunityId opportunityId,
    LossReason lossReason,
    String notes) {

  public static CloseOpportunityLostCommand from(
      String id,
      String lossReason,
      String lossReasonDetails,
      String notes) {

    LossReason reason = LossReason.create(lossReason, lossReasonDetails);
    return new CloseOpportunityLostCommand(OpportunityId.of(id), reason, notes);
  }

  public CloseOpportunityLostCommand {
    if (opportunityId == null) {
      throw new IllegalArgumentException("OpportunityId cannot be null");
    }

    if (lossReason == null) {
      throw new IllegalArgumentException("LossReason cannot be null");

    }
  }
}
