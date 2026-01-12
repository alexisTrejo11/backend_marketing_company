package at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.dto.output;

import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskPriority;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record TaskOutput(
		String id,
		String title,
		String description,
		String status,
		TaskPriority priority,
		String assigneeId,
		String customerId,
		String opportunityId,
		OffsetDateTime dueDate,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt,
		OffsetDateTime deletedAt,
		Integer version
) {

}