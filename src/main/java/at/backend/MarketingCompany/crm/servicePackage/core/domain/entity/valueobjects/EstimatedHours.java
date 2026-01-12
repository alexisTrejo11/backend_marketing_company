package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions.EstimatedHoursException;
import at.backend.MarketingCompany.shared.domain.exceptions.MissingFieldException;

public record EstimatedHours(Integer hours) {
  public static EstimatedHours none() {
    return new EstimatedHours(null);
  }

  public static EstimatedHours create(Integer hours) {
    var estimatedHours = new EstimatedHours(hours);
    estimatedHours.validate();
    return estimatedHours;
  }

  public void validate() {
    if (hours == null) {
      throw new MissingFieldException("EstimatedHours", "hours");
    }
    if (hours < 0) {
      throw new EstimatedHoursException("Estimated hours cannot be negative");
    }
    if (hours > 1000) {
      throw new EstimatedHoursException("Estimated hours must not exceed 1000");
    }
  }

}
