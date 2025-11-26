package at.backend.MarketingCompany.crm.deal.v2.application.dto.command;

import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.EmployeeId;

import java.math.BigDecimal;

public record SignDealCommand(
    DealId dealId,
    BigDecimal finalAmount,
    String terms,
    EmployeeId campaignManagerId
) {}