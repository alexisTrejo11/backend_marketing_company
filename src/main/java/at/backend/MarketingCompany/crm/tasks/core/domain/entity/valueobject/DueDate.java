package at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject;

import at.backend.MarketingCompany.crm.tasks.core.domain.exceptions.TaskValidationException;

import java.time.LocalDateTime;

public record DueDate(LocalDateTime value) {
    public DueDate {
        if (value != null && value.isBefore(LocalDateTime.now())) {
            throw new TaskValidationException("Due date cannot be in the past");
        }
        
        if (value != null && value.isAfter(LocalDateTime.now().plusYears(1))) {
            throw new TaskValidationException("Due date cannot be more than one year in the future");
        }
    }

    public boolean isPast() {
        return value != null && value.isBefore(LocalDateTime.now());
    }

    public boolean isOverdue() {
        return isPast();
    }

    public static DueDate from(LocalDateTime dateTime) {
        return dateTime != null ? new DueDate(dateTime) : null;
    }
}
