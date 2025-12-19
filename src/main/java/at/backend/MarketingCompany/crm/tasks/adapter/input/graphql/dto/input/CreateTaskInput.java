package at.backend.MarketingCompany.crm.tasks.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.tasks.core.application.commands.CreateTaskCommand;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.DueDate;
import at.backend.MarketingCompany.crm.tasks.core.domain.entity.valueobject.TaskPriority;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateTaskInput(
	@NotBlank String customerCompanyId,
	@NotNull LocalDateTime dueDate,
	@NotBlank TaskPriority priority,
	@NotBlank String title,
	String opportunityId,
	String description,
	String assignedTo
) {
	public CreateTaskCommand toCommand() {
		return new CreateTaskCommand(
				CustomerCompanyId.of(customerCompanyId),
				opportunityId != null ? OpportunityId.of(opportunityId) : null,
				title,
				description,
				new DueDate(dueDate),
				priority,
				assignedTo != null ? EmployeeId.of(assignedTo) : null
		);
	}
}
