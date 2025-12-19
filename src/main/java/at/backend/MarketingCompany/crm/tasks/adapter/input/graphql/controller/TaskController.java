package at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.controller;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.dto.input.TaskFilterInput;
import at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.dto.input.UpdateTaskDetailsInput;
import at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.dto.output.TaskOutput;
import at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.dto.input.CreateTaskInput;
import at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.mapper.TaskResponseMapper;
import at.backend.MarketingCompany.crm.tasks.core.application.commands.*;
import at.backend.MarketingCompany.crm.tasks.core.application.queries.*;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.Task;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskStatus;
import at.backend.MarketingCompany.crm.tasks.core.port.input.TaskCommandService;
import at.backend.MarketingCompany.crm.tasks.core.port.input.TaskQueryService;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class TaskController {
	private final TaskCommandService taskCommandService;
	private final TaskQueryService taskQueryService;
	private final TaskResponseMapper taskResponseMapper;


	@QueryMapping
	@GraphQLRateLimit
	public TaskOutput task(@Argument @Valid @NotBlank String id) {
		var query = GetTaskByIdQuery.of(id);
		Task task = taskQueryService.getTaskById(query);
		return taskResponseMapper.toOutput(task);
	}

	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<TaskOutput> tasks(
			@Argument @Valid @NotNull PageInput pageInput,
			@Argument @NotNull TaskFilterInput filterInput
	) {
		SearchTasksQuery query = filterInput.toQuery(pageInput.toPageable());
		Page<Task> taskPage = taskQueryService.searchTasks(query);
		return taskResponseMapper.toOutputPage(taskPage);
	}


	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<TaskOutput> tasksByUser(
			@Argument @Valid @NotBlank String userId,
			@Argument @Valid @NotNull PageInput pageInput
	) {
		var query = GetTasksByAssigneeQuery.of(userId, pageInput.toPageable());
		Page<Task> taskPage = taskQueryService.getTasksByAssignee(query);
		return taskResponseMapper.toOutputPage(taskPage);
	}

	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<TaskOutput> tasksByCompany(
			@Argument @Valid @NotBlank String companyId,
			@Argument @Valid @NotNull PageInput pageInput
	) {
		var query = GetTasksByCustomerQuery.of(companyId, pageInput.toPageable());
		Page<Task> taskPage = taskQueryService.getTasksByCustomer(query);
		return taskResponseMapper.toOutputPage(taskPage);
	}

	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<TaskOutput> tasksByStatus(
			@Argument @Valid @NotNull TaskStatus status,
			@Argument @Valid @NotNull PageInput pageInput
	) {
		var query = GetTasksByStatusQuery.of(status, pageInput.toPageable());
		Page<Task> taskPage = taskQueryService.getTasksByStatus(query);
		return taskResponseMapper.toOutputPage(taskPage);
	}

	@QueryMapping
	@GraphQLRateLimit
	public PageResponse<TaskOutput> overdueTasks(@Argument @Valid @NotNull PageInput pageInput) {
		var query = new GetOverdueTasksQuery(pageInput.toPageable());
		Page<Task> taskPage = taskQueryService.getOverdueTasks(query);
		return taskResponseMapper.toOutputPage(taskPage);
	}

	@MutationMapping
	@GraphQLRateLimit
	public TaskOutput createTask(@Argument @Valid @NotNull CreateTaskInput input) {
		var command = input.toCommand();
		Task task = taskCommandService.createTask(command);
		return taskResponseMapper.toOutput(task);
	}

	@MutationMapping
	@GraphQLRateLimit("sensitive")
	public TaskOutput updateTask(@Argument @Valid @NotNull UpdateTaskDetailsInput input) {
		var command = input.toCommand();
		Task task = taskCommandService.updateTask(command);
		return taskResponseMapper.toOutput(task);
	}


	@MutationMapping
	@GraphQLRateLimit
	public TaskOutput completeTasks(@Argument @Valid @NotNull String id) {
		var command = CompleteTaskCommand.of(id);
		Task task = taskCommandService.completeTask(command);
		return taskResponseMapper.toOutput(task);
	}

	@MutationMapping
	@GraphQLRateLimit
	public TaskOutput assignTask(@Argument @Valid @NotNull String id, @Argument @Valid @NotNull String userId) {
		var command = AssignTaskCommand.of(id, userId);
		Task task = taskCommandService.assingTask(command);
		return taskResponseMapper.toOutput(task);
	}

	@MutationMapping
	@GraphQLRateLimit
	public TaskOutput unassignTask(@Argument @Valid @NotNull String id) {
		var command = UnassignTaskCommand.of(id);
		Task task = taskCommandService.unassignTask(command);
		return taskResponseMapper.toOutput(task);
	}

	@MutationMapping
	@GraphQLRateLimit
	public TaskOutput updateTaskStatus(@Argument @Valid @NotNull String id, @Argument @Valid @NotNull TaskStatus status) {
		Task taskUpdated = switch (status) {
			case CANCELLED -> taskCommandService.cancelTask(CancelTaskCommand.of(id));
			case PENDING -> taskCommandService.reopenTask(ReopenTaskCommand.of(id));
			case IN_PROGRESS -> taskCommandService.markTaskInProgress(MarkTaskInProgressCommand.of(id));
			case COMPLETED -> taskCommandService.completeTask(CompleteTaskCommand.of(id));
			default -> throw new IllegalArgumentException("task status not supported for update");
		};

		return taskResponseMapper.toOutput(taskUpdated);
	}

	@MutationMapping
	@GraphQLRateLimit("sensitive")
	public Boolean deleteTask(@Argument @Valid @NotNull String id) {
		var command = DeleteTaskCommand.of(id);
		taskCommandService.deleteTask(command);
		return true;
	}
}
