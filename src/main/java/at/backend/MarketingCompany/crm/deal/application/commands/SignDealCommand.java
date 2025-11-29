package at.backend.MarketingCompany.crm.deal.application.commands;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.FinalAmount;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;

import java.math.BigDecimal;
import java.util.UUID;

public record SignDealCommand(
    DealId dealId,
    FinalAmount finalAmount,
    String terms,
    EmployeeId campaignManagerId
) {
    public static SignDealCommand from(String dealId, BigDecimal finalAmount, String terms, UUID campaignManagerId) {
        return new SignDealCommand(
            new DealId(dealId),
            new FinalAmount(finalAmount),
            terms,
            new EmployeeId(campaignManagerId.toString())
        );
    }
}