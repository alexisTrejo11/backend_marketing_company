package at.backend.MarketingCompany.crm.deal.core.application.commands;

import java.time.LocalDate;
import java.util.UUID;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;

public record CompleteDealCommand(
    DealId dealId,
    LocalDate endDate,
    String deliverables) {
  public static CompleteDealCommand from(UUID dealId, LocalDate endDate, String deliverables) {
    return new CompleteDealCommand(new DealId(dealId.toString()), endDate, deliverables);
  }
}
