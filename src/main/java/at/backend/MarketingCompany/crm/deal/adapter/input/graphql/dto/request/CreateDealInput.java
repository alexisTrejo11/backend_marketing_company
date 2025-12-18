package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import at.backend.MarketingCompany.crm.deal.core.application.commands.CreateDealCommand;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateDealInput(
    String opportunityId,
    List<String> servicePackageIds,
    LocalDate startDate) {


	public CreateDealCommand toCommand() {
		return CreateDealCommand.from(
				null, // customerCompanyId se obtendrá de la opportunity
				opportunityId,
				servicePackageIds,
				startDate);
	}
}
