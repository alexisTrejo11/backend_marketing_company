package at.backend.MarketingCompany.crm.deal.repository.dto.input;

import java.math.BigDecimal;
import java.util.UUID;

public record SignDealInput(
    UUID dealId,
    BigDecimal finalAmount,
    String terms,
    UUID campaignManagerId
) {}
