package at.backend.MarketingCompany.crm.deal.v2.application.dto.command;

import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.DealId;

public record MarkDealAsPaidCommand(DealId dealId) {}