package at.backend.MarketingCompany.customer.application.dto.query;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record HasCompetitorsQuery(CustomerId id) {
  public static HasCompetitorsQuery of(String id) {
    return new HasCompetitorsQuery(CustomerId.of(id));
  }
}
