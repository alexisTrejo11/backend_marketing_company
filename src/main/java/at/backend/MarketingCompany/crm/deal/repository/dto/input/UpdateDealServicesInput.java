package at.backend.MarketingCompany.crm.deal.repository.dto.input;

import java.util.List;
import java.util.UUID;

public record UpdateDealServicesInput(
    UUID dealId,
    List<UUID> servicePackageIds
) {}
