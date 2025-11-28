package at.backend.MarketingCompany.crm.tasks.domain.entity.valueobject;

import java.util.UUID;

public record TaskId(String value) {
    public TaskId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Task ID cannot be null or blank");
        }
    }

    public static TaskId create() {
        return new TaskId(UUID.randomUUID().toString());
    }

    public static TaskId from(String value) {
        return new TaskId(value);
    }

    public static TaskId from(UUID value) {
        return new TaskId(value.toString());
    }
}
