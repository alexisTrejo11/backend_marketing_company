package at.backend.MarketingCompany.crm.deal.repository.dto.input;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DealFilterInput(
    List<String> statuses,
    UUID customerId,
    UUID campaignManagerId,
    LocalDate startDate,
    LocalDate endDate
) {}
