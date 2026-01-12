package at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.tasks.core.application.commands.UpdateTaskDetailsCommand;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskId;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskPriority;

public record UpdateTaskDetailsInput(
		String taskId,
    String title,
    String description,
    TaskPriority priority
) {
	public UpdateTaskDetailsCommand toCommand() {
		return new UpdateTaskDetailsCommand(
				TaskId.of(taskId),
				title,
				description,
				priority
		);
	}
}