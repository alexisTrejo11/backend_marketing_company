package at.backend.MarketingCompany.crm.interaction.application.queries;

import org.springframework.data.domain.Pageable;

public record GetTodayInteractionsQuery(Pageable pageable) {}
