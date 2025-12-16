package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto;

import java.time.LocalDate;
import java.util.List;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.core.application.dto.QuoteCreateCommand;
import at.backend.MarketingCompany.crm.quote.core.application.dto.QuoteItemCreateCommand;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record CreateQuoteInput(
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
