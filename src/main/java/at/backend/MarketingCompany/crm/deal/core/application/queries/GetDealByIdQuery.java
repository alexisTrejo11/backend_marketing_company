package at.backend.MarketingCompany.crm.deal.core.application.queries;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;

public record GetDealByIdQuery(DealId dealId) {
  public static GetDealByIdQuery from(String id) {
    return new GetDealByIdQuery(new DealId(id));
  }
}
