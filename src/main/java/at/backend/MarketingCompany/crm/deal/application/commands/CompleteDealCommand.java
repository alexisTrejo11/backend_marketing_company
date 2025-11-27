package at.backend.MarketingCompany.crm.deal.application.commands;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;

import java.time.LocalDate;
import java.util.UUID;

public record CompleteDealCommand(
    DealId dealId,
    LocalDate endDate,
    String deliverables
) {
    public static CompleteDealCommand from(UUID dealId, LocalDate endDate, String deliverables) {
        return new CompleteDealCommand(new DealId(dealId.toString()), endDate, deliverables);
    }
}