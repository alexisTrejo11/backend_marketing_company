package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web.dto;

import java.time.LocalDate;
import java.util.List;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteCreateCommand;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteItemCreateCommand;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record QuoteInput(
    @NotNull String customerId,
    String opportunityId,
    @NotNull @Future LocalDate validUntil,
    List<QuoteItemInput> items) {

  public QuoteCreateCommand toCommand() {

    return new QuoteCreateCommand(
        new CustomerCompanyId(customerId),
        opportunityId != null ? new OpportunityId(opportunityId) : null,
        validUntil,
        toItemCommands());
  }

  public List<QuoteItemCreateCommand> toItemCommands() {
    if (items == null) {
      return List.of();
    }
    return items.stream()
        .map(QuoteItemInput::toCommand)
        .toList();
  }
}
