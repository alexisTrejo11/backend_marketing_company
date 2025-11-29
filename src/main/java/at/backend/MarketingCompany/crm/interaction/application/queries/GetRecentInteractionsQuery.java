package at.backend.MarketingCompany.crm.interaction.application.queries;

import org.springframework.data.domain.Pageable;

public record GetRecentInteractionsQuery(int days, Pageable pageable) {}
