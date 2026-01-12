package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import java.math.BigDecimal;

import at.backend.MarketingCompany.crm.deal.core.application.commands.SignDealCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SignDealInput(
    @NotBlank String dealId,
    @Positive BigDecimal finalAmount,
    String terms,
    String campaignManagerId) {

  public SignDealCommand toCommand() {
    return SignDealCommand.from(
        dealId(),
        finalAmount(),
        terms(),
        campaignManagerId());
  }
}
