package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record DeleteTaskCommand(TaskId taskId) {
	public static DeleteTaskCommand of(String taskId) {
		return new DeleteTaskCommand(TaskId.of(taskId));
	}
}
