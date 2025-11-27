package at.backend.MarketingCompany.crm.deal.application.commands;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;

import java.util.UUID;

public record CancelDealCommand(DealId dealId) {
    public static CancelDealCommand from(UUID id) {
        return new CancelDealCommand(new DealId(id.toString()));
    }
}