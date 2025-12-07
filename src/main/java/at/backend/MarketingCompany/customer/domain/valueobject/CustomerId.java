package at.backend.MarketingCompany.customer.domain.valueobject;

import java.util.UUID;

public record CustomerId(String value) {
  public static CustomerId of(String id) {
    return new CustomerId(id);
  }

  public static CustomerId generate() {
    return new CustomerId(UUID.randomUUID().toString());
  }
}
