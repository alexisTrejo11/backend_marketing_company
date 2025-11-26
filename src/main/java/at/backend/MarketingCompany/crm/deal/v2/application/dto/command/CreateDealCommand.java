package at.backend.MarketingCompany.crm.deal.v2.application.dto.command;

import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.CustomerId;
import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.OpportunityId;
import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.ServiceId;

import java.time.LocalDate;
import java.util.List;

public record CreateDealCommand(
    CustomerId customerId,
    OpportunityId opportunityId,
    List<ServiceId> servicePackageIds,
    LocalDate startDate
) {}