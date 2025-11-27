package at.backend.MarketingCompany.crm.deal.repository.dto.input;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateDealInput(
    UUID opportunityId,
    List<String> servicePackageIds,
    LocalDate startDate
) {}

