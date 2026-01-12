package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions.RecurrenceInfoException;
import at.backend.MarketingCompany.shared.domain.exceptions.MissingFieldException;

public record RecurrenceInfo(Boolean isRecurring, Frequency frequency) {
  public static RecurrenceInfo none() {
    return new RecurrenceInfo(false, null);
  }

  public static RecurrenceInfo create(Boolean isRecurring, Frequency frequency) {
    if (isRecurring == null) {
      throw new MissingFieldException("RecurrenceInfo", "isRecurring");
    }

    if (isRecurring && frequency == null) {
      throw new RecurrenceInfoException("Frequency must be provided for recurring services");
    }

    if (!isRecurring && frequency != null) {
      throw new RecurrenceInfoException("Frequency should not be set for non-recurring services");
    }

    return new RecurrenceInfo(isRecurring, frequency);
  }
}
