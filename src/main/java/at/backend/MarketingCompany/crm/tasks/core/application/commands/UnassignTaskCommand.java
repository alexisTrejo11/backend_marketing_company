package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record UnassignTaskCommand(TaskId taskId) {
	public static UnassignTaskCommand of(String taskId) {
		return new UnassignTaskCommand(TaskId.of(taskId));
	}
}
