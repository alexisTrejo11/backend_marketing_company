package at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects;

import at.backend.MarketingCompany.crm.servicePackage.domain.exceptions.ProjectDurationException;

public record ProjectDuration(Integer months) {
    public ProjectDuration {
        if (months != null && months > 36) {
            throw new ProjectDurationException("Project duration must not exceed 36 months");
        }
        if (months != null && months < 0) {
            throw new ProjectDurationException("Project duration cannot be negative");
        }
    }

    public static ProjectDuration of(Integer months) {
        return new ProjectDuration(months);
    }
}