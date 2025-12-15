package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GetDealsByCustomerInput(
    @NotBlank String customerId,
    @NotNull PageInput pageInput) {

}
