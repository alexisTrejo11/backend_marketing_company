package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import at.backend.MarketingCompany.crm.deal.core.application.commands.CreateDealCommand;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public record CreateDealInput(
    String opportunityId,
    List<String> servicePackageIds,
    @NotBlank String customerCompanyId,
    LocalDate startDate) {

  public CreateDealCommand toCommand() {
    return CreateDealCommand.from(
        customerCompanyId,
        opportunityId,
        servicePackageIds,
        startDate);
  }
}
