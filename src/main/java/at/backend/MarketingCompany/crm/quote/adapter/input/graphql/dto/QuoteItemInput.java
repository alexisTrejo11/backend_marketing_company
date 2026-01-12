package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto;

import at.backend.MarketingCompany.crm.quote.core.application.commands.QuoteItemCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import jakarta.validation.constraints.NotNull;

public record QuoteItemInput(
    @NotNull String servicePackageId) {

  public QuoteItemCommand toCommand() {
    return new QuoteItemCommand(
        ServicePackageId.of(servicePackageId));
  }
}
