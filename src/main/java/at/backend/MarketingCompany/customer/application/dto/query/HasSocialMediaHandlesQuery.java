package at.backend.MarketingCompany.customer.application.dto.query;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public record HasSocialMediaHandlesQuery(CustomerId id) {
  public static HasSocialMediaHandlesQuery of(String id) {
    return new HasSocialMediaHandlesQuery(CustomerId.of(id));
  }
}
