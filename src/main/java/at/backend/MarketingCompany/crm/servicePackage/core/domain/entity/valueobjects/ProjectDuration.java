package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions.ProjectDurationException;

public record ProjectDuration(Integer months) {

  public static ProjectDuration none() {
    return new ProjectDuration(null);
  }

  public static ProjectDuration create(Integer months) {
    if (months != null && months > 36) {
      throw new ProjectDurationException("Project duration must not exceed 36 months");
    }
    if (months != null && months < 0) {
      throw new ProjectDurationException("Project duration cannot be negative");
    }
    return new ProjectDuration(months);
  }
}
