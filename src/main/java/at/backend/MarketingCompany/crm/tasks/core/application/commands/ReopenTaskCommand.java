package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record ReopenTaskCommand(TaskId taskId) {
	public static ReopenTaskCommand of(String taskId) {
		return new ReopenTaskCommand(TaskId.of(taskId));
	}
}
