package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import java.util.List;
import java.util.UUID;

public record UpdateDealServicesInput(
    UUID dealId,
    List<UUID> servicePackageIds) {
}
