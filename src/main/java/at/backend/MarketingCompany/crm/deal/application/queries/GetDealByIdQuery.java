package at.backend.MarketingCompany.crm.deal.application.queries;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;

import java.util.UUID;

public record GetDealByIdQuery(DealId dealId) {
    public static GetDealByIdQuery from(UUID id) {
        return new GetDealByIdQuery(new DealId(id.toString()));
    }
}
