package at.backend.MarketingCompany.crm.deal.core.application.commands;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;

public record StartDealExecutionCommand(DealId dealId) {
  public static StartDealExecutionCommand from(String dealId) {
    return new StartDealExecutionCommand(new DealId(dealId));
  }
}
