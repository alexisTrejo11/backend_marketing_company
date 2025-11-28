package at.backend.MarketingCompany.crm.servicePackage.v2.domain.entity.valueobjects;

import at.backend.MarketingCompany.common.exceptions.MissingFieldException;
import at.backend.MarketingCompany.crm.servicePackage.v2.domain.exceptions.EstimatedHoursException;

public record EstimatedHours(Integer hours)
{
    public EstimatedHours {
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

    public static EstimatedHours of(Integer hours) {
        return new EstimatedHours(hours);
    }
}