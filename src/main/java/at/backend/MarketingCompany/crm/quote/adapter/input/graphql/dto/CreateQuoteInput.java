package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto;

import java.time.LocalDate;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.core.application.commands.*;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateQuoteInput(
    @NotNull String customerId,
    @NotNull String validUntil,
    @Size(max = 1000) String notes,
    @Size(max = 5000) String termsAndConditions,
    String opportunityId) {

  public CreateQuoteCommand toCommand() {
    return CreateQuoteCommand.builder()
        .customerCompanyId(CustomerCompanyId.of(customerId))
        .validUntil(LocalDate.parse(validUntil))
        .notes(notes)
        .termsAndConditions(termsAndConditions)
        .opportunityId(opportunityId != null ? OpportunityId.of(opportunityId) : null)
        .build();
  }
}
