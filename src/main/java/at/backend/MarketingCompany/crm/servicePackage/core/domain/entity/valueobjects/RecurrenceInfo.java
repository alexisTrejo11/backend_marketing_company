package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects;

import at.backend.MarketingCompany.shared.domain.exceptions.MissingFieldException;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions.RecurrenceInfoException;

public record RecurrenceInfo(Boolean isRecurring, Frequency frequency) {
    public RecurrenceInfo {
        if (isRecurring == null) {
            throw new MissingFieldException("RecurrenceInfo", "isRecurring");
        }

        if (isRecurring && frequency == null) {
            throw new RecurrenceInfoException("Frequency must be provided for recurring services");
        }

        if (!isRecurring && frequency != null) {
            throw new RecurrenceInfoException("Frequency should not be set for non-recurring services");
        }

    }

    public static RecurrenceInfo create(Boolean isRecurring, Frequency frequency) {
        return new RecurrenceInfo(isRecurring, frequency);
    }
}