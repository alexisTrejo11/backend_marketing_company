package at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.dto.output.TaskOutput;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.Task;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskResponseMapper {

	public TaskOutput toOutput(Task task) {
		if (task == null) {
			return null;
		}

		return TaskOutput.builder()
				.id(task.getId().asString())
				.title(task.getTitle())
				.description(task.getDescription().orElse(null))
				.status(task.getStatus().name())
				.priority(task.getPriority())
				.assigneeId(task.getAssignedTo().isPresent() ? task.getAssignedTo().get().asString() : null)
				.customerId(task.getCustomerCompanyId() != null ? task.getCustomerCompanyId().asString() : null)
				.opportunityId(task.getOpportunityId() != null ? task.getOpportunityId().asString() : null)
				.dueDate(task.getDueDate().isPresent() ? task.getDueDate().get().value().atOffset(java.time.ZoneOffset.UTC) : null)
				.createdAt(task.getCreatedAt() != null ? task.getCreatedAt().atOffset(java.time.ZoneOffset.UTC) : null)
				.updatedAt(task.getUpdatedAt() != null ? task.getUpdatedAt().atOffset(java.time.ZoneOffset.UTC) : null)
				.deletedAt(task.getDeletedAt() != null ? task.getDeletedAt().atOffset(java.time.ZoneOffset.UTC) : null)
				.version(task.getVersion())
				.build();
	}

	public List<TaskOutput> toOutputList(List<Task> tasks) {
		if (tasks == null) {
			return List.of();
		}

		return tasks.stream()
				.map(this::toOutput)
				.toList();
	}

	public PageResponse<TaskOutput> toOutputPage(Page<Task> taskPage) {
		if (taskPage == null) {
			return PageResponse.empty();
		}

		Page<TaskOutput> taskOutputs = taskPage.map(this::toOutput);
		return PageResponse.of(taskOutputs);
	}
}
