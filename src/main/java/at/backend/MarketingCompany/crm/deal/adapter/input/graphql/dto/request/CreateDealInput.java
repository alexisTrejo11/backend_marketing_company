package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateDealInput(
    String opportunityId,
    List<String> servicePackageIds,
    LocalDate startDate) {
}
