package at.backend.MarketingCompany.crm.tasks.core.application.queries;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;

public record GetTaskByIdQuery(TaskId taskId) {
	public static GetTaskByIdQuery of(String id) {
		return new GetTaskByIdQuery(TaskId.of(id));
	}
}

