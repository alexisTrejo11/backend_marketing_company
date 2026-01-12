package at.backend.MarketingCompany.crm.deal.core.application.commands;

import java.time.LocalDate;
import java.util.UUID;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;

public record CompleteDealCommand(
    DealId dealId,
    LocalDate endDate,
    String deliverables) {
  public static CompleteDealCommand from(String dealId, LocalDate endDate, String deliverables) {
    return new CompleteDealCommand(DealId.of(dealId), endDate, deliverables);
  }
}
