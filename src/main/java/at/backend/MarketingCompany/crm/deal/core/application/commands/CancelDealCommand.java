package at.backend.MarketingCompany.crm.deal.core.application.commands;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;

public record CancelDealCommand(DealId dealId) {
  public static CancelDealCommand from(String id) {
    return new CancelDealCommand(new DealId(id));
  }
}
