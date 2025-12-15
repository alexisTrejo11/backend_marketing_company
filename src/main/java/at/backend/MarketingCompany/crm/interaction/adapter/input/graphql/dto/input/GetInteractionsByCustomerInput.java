package at.backend.MarketingCompany.crm.interaction.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetInteractionsByCustomerQuery;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GetInteractionsByCustomerInput(@NotBlank String customerId, @NotNull PageInput pageInput) {

  public GetInteractionsByCustomerQuery toQuery() {
    return GetInteractionsByCustomerQuery.from(customerId, pageInput.toPageable());
  }
}
