package at.backend.MarketingCompany.crm.deal.core.application.commands;

import java.math.BigDecimal;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.FinalAmount;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;

public record SignDealCommand(
    DealId dealId,
    FinalAmount finalAmount,
    String terms,
    EmployeeId campaignManagerId) {
  public static SignDealCommand from(String dealId, BigDecimal finalAmount, String terms, String campaignManagerId) {
    return new SignDealCommand(
        new DealId(dealId),
        new FinalAmount(finalAmount),
        terms,
        new EmployeeId(campaignManagerId));
  }
}
