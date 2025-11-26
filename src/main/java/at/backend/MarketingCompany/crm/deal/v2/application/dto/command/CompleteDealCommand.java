package at.backend.MarketingCompany.crm.deal.v2.application.dto.command;

import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.DealId;

import java.time.LocalDate;

public record CompleteDealCommand(
    DealId dealId,
    LocalDate endDate,
    String deliverables
) {}