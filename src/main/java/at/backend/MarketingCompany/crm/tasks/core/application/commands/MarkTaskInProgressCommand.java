package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record MarkTaskInProgressCommand(TaskId taskId) {
	public static MarkTaskInProgressCommand of(String taskId) {
		return new MarkTaskInProgressCommand(TaskId.of(taskId));
	}
}
