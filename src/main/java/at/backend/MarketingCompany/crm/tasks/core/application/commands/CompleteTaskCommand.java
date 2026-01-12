package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record CompleteTaskCommand(TaskId taskId) {
	public static CompleteTaskCommand of(String taskId) {
		return new CompleteTaskCommand(TaskId.of(taskId));
	}
}
