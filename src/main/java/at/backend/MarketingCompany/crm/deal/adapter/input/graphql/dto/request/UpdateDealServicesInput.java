package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import java.util.List;
import java.util.UUID;

public record UpdateDealServicesInput(
    String dealId,
    List<String> servicePackageIds) {
}
