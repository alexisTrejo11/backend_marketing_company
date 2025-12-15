package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto;

import java.math.BigDecimal;

import at.backend.MarketingCompany.crm.quote.core.application.dto.QuoteItemCreateCommand;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record QuoteItemInput(
    @NotNull String servicePackageId,
    @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal discountPercentage) {

  public QuoteItemCreateCommand toCommand() {
    return new QuoteItemCreateCommand(
        ServicePackageId.of(servicePackageId),
        discountPercentage);
  }
}
