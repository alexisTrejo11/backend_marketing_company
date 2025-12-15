package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record CompleteDealInput(
    UUID dealId,
    LocalDate endDate,
    String deliverables) {
}
