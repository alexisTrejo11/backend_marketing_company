package at.backend.MarketingCompany.crm.deal.v2.application.dto.command;

import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.ServiceId;

import java.util.List;

public record UpdateDealServicesCommand(
    DealId dealId,
    List<ServiceId> servicePackageIds
) {}