package at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject;

import at.backend.MarketingCompany.shared.domain.NumericId;

import java.util.UUID;

public class TaskId extends NumericId {
	public TaskId(Long value) {
		super(value);
	}

	public static TaskId of(String id) {
		return NumericId.fromString(id, TaskId::new);
	}

	// Database will generate the ID
	public static TaskId generate() {
		return new TaskId(null);
	}
}