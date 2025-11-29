package at.backend.MarketingCompany.crm.interaction.application.queries;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public record GetInteractionsByDateRangeQuery(
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable) {}
