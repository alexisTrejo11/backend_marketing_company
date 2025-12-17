package at.backend.MarketingCompany.crm.deal.core.application.commands;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;

public record MarkDealAsPaidCommand(DealId dealId) {
  public static MarkDealAsPaidCommand from(String dealId) {
    return new MarkDealAsPaidCommand(DealId.of(dealId));
  }
}
