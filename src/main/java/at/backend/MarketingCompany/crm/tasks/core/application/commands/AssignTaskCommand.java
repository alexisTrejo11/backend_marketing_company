package at.backend.MarketingCompany.crm.tasks.core.application.commands;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record AssignTaskCommand(
    TaskId taskId,
    EmployeeId assignedTo) {

	public static AssignTaskCommand of(String taskId, String assignedTo) {
		return new AssignTaskCommand(
			TaskId.of(taskId),
			EmployeeId.of(assignedTo)
		);
	}
}
