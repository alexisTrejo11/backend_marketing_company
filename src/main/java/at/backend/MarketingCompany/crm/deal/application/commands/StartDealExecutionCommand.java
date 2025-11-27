package at.backend.MarketingCompany.crm.deal.application.commands;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;

import java.util.UUID;

public record StartDealExecutionCommand(DealId dealId) {
    public static StartDealExecutionCommand from(UUID dealId) {
        return new StartDealExecutionCommand(new DealId(dealId.toString()));
    }
}