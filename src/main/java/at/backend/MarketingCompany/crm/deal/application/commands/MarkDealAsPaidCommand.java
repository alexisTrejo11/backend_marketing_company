package at.backend.MarketingCompany.crm.deal.application.commands;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;

import java.util.UUID;

public record MarkDealAsPaidCommand(DealId dealId) {
    public static MarkDealAsPaidCommand from(UUID dealId) {
        return new MarkDealAsPaidCommand(new DealId(dealId.toString()));
    }
}