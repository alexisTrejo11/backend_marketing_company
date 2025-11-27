package at.backend.MarketingCompany.crm.deal.repository.dto.input;

import java.time.LocalDate;
import java.util.UUID;

public record CompleteDealInput(
    UUID dealId,
    LocalDate endDate,
    String deliverables
) {}
