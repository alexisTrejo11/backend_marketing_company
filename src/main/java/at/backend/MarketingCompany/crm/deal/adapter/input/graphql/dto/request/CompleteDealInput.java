package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record CompleteDealInput(
    String dealId,
    LocalDate endDate,
    String deliverables) {
}
