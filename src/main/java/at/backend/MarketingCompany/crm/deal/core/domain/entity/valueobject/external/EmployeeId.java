package at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external;

import java.util.UUID;

public record EmployeeId(Long value) {
  public EmployeeId {
    if (value == null) {
      throw new IllegalArgumentException("Deal ID cannot be null.");
    }
  }

  public static  EmployeeId of(String id) {
    try {
      Long longId = Long.parseLong(id);
      return new EmployeeId(longId);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid Employee ID format: " + id);
    }
  }
  public static EmployeeId generate() {
    return new EmployeeId(null);
  }
}
