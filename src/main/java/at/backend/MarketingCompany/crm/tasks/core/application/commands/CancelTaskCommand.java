package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record CancelTaskCommand(TaskId taskId) {
	public static CancelTaskCommand of(String taskId) {
		return new CancelTaskCommand(TaskId.of(taskId));
	}
}
