package at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.tasks.core.application.queries.SearchTasksQuery;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskPriority;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskStatus;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public record TaskFilterInput(
		String searchTerm,
		Set<TaskStatus> statuses,
		Set<TaskPriority> priorities,
		String customerId,
		String assigneeId,
		Boolean overdueOnly
) {

	public SearchTasksQuery toQuery(Pageable  pageable) {
		return SearchTasksQuery.builder()
				.searchTerm(this.searchTerm)
				.statuses(this.statuses)
				.priorities(this.priorities)
				.customerId(this.customerId)
				.assigneeId(this.assigneeId)
				.overdueOnly(this.overdueOnly)
				.pageable(pageable)
				.build();
	}
}
